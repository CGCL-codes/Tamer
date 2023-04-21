package br.com.safehibernate.provider;

public interface ICertificateProvider {

    byte[] encrypt(byte[] source);

    byte[] decrypt(byte[] source);
}
