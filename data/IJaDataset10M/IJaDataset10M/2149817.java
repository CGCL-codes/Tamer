package com.aelitis.azureus.core.security;

import java.nio.ByteBuffer;

public interface CryptoSTSEngine {

    public void getKeys(ByteBuffer message) throws CryptoManagerException;

    public void putKeys(ByteBuffer message) throws CryptoManagerException;

    public void getAuth(ByteBuffer message) throws CryptoManagerException;

    public void putAuth(ByteBuffer message) throws CryptoManagerException;

    public byte[] getSharedSecret() throws CryptoManagerException;

    public byte[] getRemotePublicKey() throws CryptoManagerException;
}
