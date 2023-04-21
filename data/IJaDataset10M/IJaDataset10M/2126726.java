package uk.ac.ebi.mg.xchg.upload;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import com.pri.adob.FilePartADOB;

public class FileUploadClient implements Sender {

    private Recipient rcpt;

    private int chunkSize = Constants.chunkSize;

    public FileUploadClient(Recipient r) {
        rcpt = r;
    }

    public void closeTransaction(Transaction trn, boolean commit) throws UploadException {
        rcpt.closeTransaction(trn.getKey(), commit);
    }

    public Transaction continueTransaction(String key) throws UploadException {
        TransactionInfo trInf = rcpt.getTransactionInfo(key);
        if (trInf != null) return new Transaction(key, trInf);
        throw new UploadException("No such transaction: " + key, UploadException.INV_TRANSACTION_KEY);
    }

    public Transaction createTransaction() throws UploadException {
        Transaction trn = null;
        do {
            trn = new Transaction();
        } while (!rcpt.createTransaction(trn.getKey()));
        return trn;
    }

    public boolean upload(Transaction trn, String fileKey, File inputFile) throws UploadException {
        int trys = 0;
        while (!trn.isSuspended()) {
            if (trys++ > 3) return false;
            List<Chunk> chlst = rcpt.getTransactionInfo(trn.getKey()).getFileInfo(fileKey).getChunks();
            long fileSize = inputFile.length();
            boolean good = true;
            if (fileComplete(chlst, fileSize)) {
                try {
                    byte[] sumR = rcpt.getFileSum(trn.getKey(), fileKey, fileSize);
                    byte[] sumL = new FileDigest(inputFile).getDigest();
                    if (!digestEqual(sumR, sumL)) good = false;
                } catch (UploadException e) {
                    if (e.getErrorCode() == UploadException.FILE_INCOMPLETE) good = false; else throw e;
                } catch (IOException e) {
                    e.printStackTrace();
                    throw new UploadException("IO error", e, UploadException.IOError);
                }
                if (good) {
                    System.out.println("File is comleted and good. " + fileKey);
                    return true;
                }
                System.out.println("File complete but not good");
            }
            mainCyc: while (true) {
                if (chlst == null) chlst = rcpt.getTransactionInfo(trn.getKey()).getFileInfo(fileKey).getChunks();
                System.out.println("Total chunks: " + (chlst == null ? 0 : chlst.size()));
                if (chlst == null || chlst.size() == 0) {
                    System.out.println("No chunks still");
                    long end = (chunkSize < fileSize ? chunkSize : fileSize);
                    uploadChunk(trn, fileKey, inputFile, 0, end);
                    chlst = null;
                    continue;
                }
                Chunk prev = null;
                for (Chunk ch : chlst) {
                    if (prev == null) {
                        prev = ch;
                        if (ch.getBegin() != 0) {
                            System.out.println("First chunk is not file start. " + ch);
                            long end = (chunkSize < ch.getBegin() ? chunkSize : ch.getBegin());
                            uploadChunk(trn, fileKey, inputFile, 0, end);
                            chlst = null;
                            continue mainCyc;
                        }
                        try {
                            if (!good && !digestEqual(ch.getDigest(), new FileDigest(inputFile, ch.getBegin(), ch.getEnd() - ch.getBegin()).getDigest())) {
                                System.out.println("Bad chunk. " + ch);
                                rcpt.deleteChunk(trn.getKey(), fileKey, ch.getID());
                                chlst = null;
                            }
                        } catch (IOException e) {
                            throw new UploadException("IO error", e, UploadException.IOError);
                        }
                        if (ch.getEnd() == fileSize) {
                            System.out.println("First chunk is the last. Ok " + ch);
                            break mainCyc;
                        }
                        continue;
                    }
                    if (ch.getBegin() > prev.getEnd()) {
                        System.out.println("There is a hole between the chunks. " + ch + " and " + prev);
                        long end = (chunkSize < ch.getBegin() - prev.getEnd() ? chunkSize : (ch.getBegin() - prev.getEnd()));
                        uploadChunk(trn, fileKey, inputFile, prev.getEnd(), prev.getEnd() + end);
                        chlst = null;
                        prev = ch;
                        continue mainCyc;
                    } else try {
                        if (!good && !digestEqual(ch.getDigest(), new FileDigest(inputFile, ch.getBegin(), ch.getEnd() - ch.getBegin()).getDigest())) {
                            System.out.println("Bad chunk. " + ch);
                            rcpt.deleteChunk(trn.getKey(), fileKey, ch.getID());
                            chlst = null;
                            prev = ch;
                            continue mainCyc;
                        }
                    } catch (IOException e) {
                        throw new UploadException("IO error", e, UploadException.IOError);
                    }
                    prev = ch;
                }
                if (prev.getEnd() < fileSize) {
                    System.out.println("Last chunk is not the end " + prev);
                    long end = (chunkSize < fileSize - prev.getEnd() ? chunkSize : (fileSize - prev.getEnd()));
                    uploadChunk(trn, fileKey, inputFile, prev.getEnd(), prev.getEnd() + end);
                    chlst = null;
                } else break;
            }
        }
        return false;
    }

    private boolean fileComplete(List<Chunk> chlst, long fileSize) {
        if (chlst == null || chlst.size() == 0) return false;
        if (chlst.get(0).getBegin() != 0) return false;
        Chunk prev = null;
        for (Chunk ch : chlst) {
            if (prev == null) {
                if (ch.getEnd() == fileSize) return true;
                prev = ch;
                continue;
            }
            if (ch.getBegin() > prev.getEnd()) return false;
            prev = ch;
        }
        if (prev.getEnd() == fileSize) return true;
        return false;
    }

    public void upload1(Transaction trn, String fileKey, File inputFile) throws IOException, UploadException {
        List<Chunk> chlst = rcpt.getTransactionInfo(trn.getKey()).getFileInfo(fileKey).getChunks();
        Chunk prev = null;
        if (chlst != null) {
            for (Chunk cn : chlst) {
                if (prev == null) {
                    if (cn.getBegin() != 0) uploadChunk(trn, fileKey, inputFile, 0, cn.getBegin());
                } else if (cn.getBegin() > prev.getEnd()) uploadChunk(trn, fileKey, inputFile, prev.getEnd(), cn.getBegin());
                prev = cn;
            }
        }
        long last = prev == null ? 0 : prev.getEnd();
        long flen = inputFile.length();
        while (last < flen) {
            long end = last + (chunkSize < (flen - last) ? chunkSize : (flen - last));
            uploadChunk(trn, fileKey, inputFile, last, end);
            last = end;
        }
        byte[] sumR = null;
        try {
            sumR = rcpt.getFileSum(trn.getKey(), fileKey, inputFile.length());
        } catch (UploadException e) {
            e.printStackTrace();
        }
        byte[] sumL = new FileDigest(inputFile).getDigest();
        if (sumR.length == sumL.length) {
            for (int i = 0; i < sumR.length; i++) if (sumR[i] != sumL[i]) throw new IOException("Checksum failed");
        }
        System.out.println("Checksum is OK");
    }

    private void uploadChunk(Transaction trn, String fileKey, File inputFile, long begin, long end) throws UploadException {
        rcpt.uploadChunk(trn.getKey(), fileKey, begin, new FilePartADOB(inputFile, begin, end - begin), null);
    }

    private boolean digestEqual(byte[] sumR, byte[] sumL) {
        System.out.println("==" + sumL.length + "==" + sumR.length);
        System.out.println(">" + Arrays.toString(sumR));
        System.out.println(">" + Arrays.toString(sumL));
        if (sumR.length == sumL.length) {
            for (int i = 0; i < sumR.length; i++) if (sumR[i] != sumL[i]) return false;
            return true;
        }
        return false;
    }

    public void startFileTransfer(Transaction tr, String fileKey, long length) throws UploadException {
        rcpt.startFileTransfer(tr.getKey(), fileKey, length);
    }
}
