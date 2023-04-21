package net.sf.jnclib.tp.ssh2.crai;

public interface CraiCipher {

    public void initEncrypt(byte[] key, byte[] iv) throws CraiException;

    public void initDecrypt(byte[] key, byte[] iv) throws CraiException;

    public void process(byte[] in, int off, int len, byte[] out, int off_out) throws CraiException;
}
