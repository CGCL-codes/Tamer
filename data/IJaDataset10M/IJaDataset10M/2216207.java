package jpar2;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import jpar2.files.ParityFile;
import jpar2.packets.FileDescriptionPacket;
import jpar2.packets.InputFileSliceChecksumPacket;
import jpar2.packets.MainPacket;
import jpar2.packets.Packet;
import jpar2.packets.RecoverySlicePacket;
import jpar2.utility.Checksum;
import jpar2.utility.FileSliceChecksum;

public class RecoverySet {

    private MainPacket mainPacket;

    private Map<String, Checksum> fileNameToIdMap = new HashMap<String, Checksum>();

    private Map<Checksum, Checksum> checksum16kToIdMap = new HashMap<Checksum, Checksum>();

    private List<RecoverySlicePacket> recoverySlicePackets = new LinkedList<RecoverySlicePacket>();

    private Map<Checksum, FileDescriptionPacket> fileDescriptionPackets = new TreeMap<Checksum, FileDescriptionPacket>();

    private Map<Checksum, InputFileSliceChecksumPacket> inputFileSliceChecksumPackets = new HashMap<Checksum, InputFileSliceChecksumPacket>();

    private LinkedList<RecoverySlicePacket> tempRSP = new LinkedList<RecoverySlicePacket>();

    private LinkedList<FileDescriptionPacket> tempFDP = new LinkedList<FileDescriptionPacket>();

    private LinkedList<InputFileSliceChecksumPacket> tempIFSCP = new LinkedList<InputFileSliceChecksumPacket>();

    private Map<Checksum, Boolean> foundInputFiles = new HashMap<Checksum, Boolean>();

    public RecoverySet() {
    }

    public void reset() {
        mainPacket = null;
        recoverySlicePackets.clear();
        fileDescriptionPackets.clear();
        inputFileSliceChecksumPackets.clear();
    }

    public void addParityFile(ParityFile parityFile) {
        if (mainPacket == null && parityFile.hasMainPacket()) {
            mainPacket = parityFile.getMainPacket();
            processStoredPackets();
            loadPackets(parityFile);
        } else if (mainPacket == null && !parityFile.hasMainPacket()) {
            storePackets(parityFile);
        } else {
            loadPackets(parityFile);
        }
    }

    private void storePackets(ParityFile parityFile) {
        tempRSP.addAll(parityFile.getRecoverySlicePackets());
        tempFDP.addAll(parityFile.getFileDescriptionPackets());
        tempIFSCP.addAll(parityFile.getInputFileSliceChecksumPackets());
    }

    private void loadPackets(ParityFile parityFile) {
        for (RecoverySlicePacket packet : parityFile.getRecoverySlicePackets()) {
            addPacket(packet);
        }
        for (FileDescriptionPacket packet : parityFile.getFileDescriptionPackets()) {
            addPacket(packet);
        }
        for (InputFileSliceChecksumPacket packet : parityFile.getInputFileSliceChecksumPackets()) {
            addPacket(packet);
        }
    }

    private void processStoredPackets() {
        RecoverySlicePacket recoverySlicePacket;
        while ((recoverySlicePacket = tempRSP.poll()) != null) {
            addPacket(recoverySlicePacket);
        }
        FileDescriptionPacket fileDescriptionPacket;
        while ((fileDescriptionPacket = tempFDP.poll()) != null) {
            addPacket(fileDescriptionPacket);
        }
        InputFileSliceChecksumPacket inputFileSliceChecksumPacket;
        while ((inputFileSliceChecksumPacket = tempIFSCP.poll()) != null) {
            addPacket(inputFileSliceChecksumPacket);
        }
    }

    private void addPacket(RecoverySlicePacket packet) {
        if (isInSet(packet) && !recoverySlicePackets.contains(packet)) recoverySlicePackets.add(packet);
    }

    private void addPacket(FileDescriptionPacket packet) {
        if (isInSet(packet) && !fileDescriptionPackets.containsKey(packet.getFileId())) fileDescriptionPackets.put(packet.getFileId(), packet);
    }

    private void addPacket(InputFileSliceChecksumPacket packet) {
        if (isInSet(packet) && !inputFileSliceChecksumPackets.containsKey(packet.getFileId())) inputFileSliceChecksumPackets.put(packet.getFileId(), packet);
    }

    private boolean isInSet(Packet packet) {
        return mainPacket.getRecoverySetId().equals(packet.getRecoverySetId());
    }

    public boolean hasMainPacket() {
        return mainPacket != null;
    }

    public boolean isComplete() {
        boolean sucess = true;
        for (Checksum fileId : mainPacket.getRecoverableFileIds()) {
            if (!foundInputFiles.containsKey(fileId)) foundInputFiles.put(fileId, false);
            if (!fileDescriptionPackets.containsKey(fileId)) {
                sucess = false;
            } else {
                if (!fileNameToIdMap.containsKey(fileId)) fileNameToIdMap.put(fileDescriptionPackets.get(fileId).getFileName(), fileId);
                if (!checksum16kToIdMap.containsValue(fileId)) checksum16kToIdMap.put(fileDescriptionPackets.get(fileId).getFileChecksum16K(), fileId);
            }
            if (!inputFileSliceChecksumPackets.containsKey(fileId)) sucess = false;
        }
        return sucess;
    }

    public boolean isValidChecksum16k(Checksum checksum) {
        return checksum16kToIdMap.containsKey(checksum);
    }

    public boolean isValidFileName(String fileName) {
        return fileNameToIdMap.containsKey(fileName);
    }

    public double getSliceSize() {
        return mainPacket.getSliceSize();
    }

    public Checksum getFileId(Checksum checksum16k) {
        return checksum16kToIdMap.get(checksum16k);
    }

    public Checksum getFileId(String fileName) {
        return fileNameToIdMap.get(fileName);
    }

    public double getFileLength(Checksum fileId) {
        return fileDescriptionPackets.get(fileId).getFileLength();
    }

    public void foundFileId(Checksum fileId) {
        foundInputFiles.put(fileId, true);
    }

    public List<Checksum> getMissingFileIds() {
        List<Checksum> missingFileIds = new LinkedList<Checksum>();
        for (Checksum fileId : foundInputFiles.keySet()) {
            if (!foundInputFiles.get(fileId)) missingFileIds.add(fileId);
        }
        return missingFileIds;
    }

    public Checksum getFileChecksum(Checksum fileId) {
        return fileDescriptionPackets.get(fileId).getFileChecksum();
    }

    public FileSliceChecksum getFileSliceChecksum(Checksum fileId, int slice) {
        return inputFileSliceChecksumPackets.get(fileId).getFileSliceChecksum(slice);
    }

    public List<Checksum> getFileIds() {
        return mainPacket.getRecoverableFileIds();
    }

    public List<RecoverySlicePacket> getRecoverySlicePackets() {
        return recoverySlicePackets;
    }
}
