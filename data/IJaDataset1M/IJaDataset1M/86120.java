package br.wiki.chinchilla.seguranca;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import sun.misc.BASE64Decoder;
import br.wiki.chinchilla.Bundle;
import br.wiki.chinchilla.exception.SegurancaException;

/**
 * Classe com métodos de cifragem e decifragem usando chafe DES criada pela
 * própria classe e salva em disco, no endereço especificado na chave
 * <code>"chave.des"</code> do bundle do chinchilla.
 * 
 * @author fgomes
 */
public class Cifrador {

    private static final Log LOG = LogFactory.getLog(Cifrador.class);

    /**
	 * chave privada usada para criptografia
	 */
    private static SecretKey desKey = null;

    /**
	 * chave privada usada para criptografia
	 */
    private static byte[] key = null;

    /**
	 * Cifra uma String usando algoritmo DES com 56 bits de largura.
	 * 
	 * @param msg
	 *            a mesnagem a ser criptografada
	 * @return a mensagem criptografada
	 * @throws SegurancaException
	 *             quando a mensagem não pode ser criptografada
	 * @throws IOException
	 *             quando a chave secreta não pode ser salva em banco de dados
	 */
    public static String cifra(String msg) throws SegurancaException, IOException {
        if (msg == null || msg.equals("")) {
            LOG.warn("Cifrador requisitado para cifragem de String vazia ou nulla: '" + msg + "'");
            return null;
        }
        Cipher enCipher;
        try {
            enCipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
            enCipher.init(Cipher.WRAP_MODE, getKeyGen());
            key = enCipher.wrap(getKeyGen());
            enCipher.init(Cipher.ENCRYPT_MODE, getKeyGen());
            byte[] dados = null;
            try {
                dados = msg.getBytes("UTF8");
            } catch (UnsupportedEncodingException e) {
                LOG.warn(e);
                throw new SegurancaException(e);
            }
            byte[] dadoEncriptado = enCipher.doFinal(dados);
            return new sun.misc.BASE64Encoder().encode(dadoEncriptado);
        } catch (NoSuchAlgorithmException e) {
            LOG.warn(e);
            throw new SegurancaException(e);
        } catch (NoSuchPaddingException e) {
            LOG.warn(e);
            throw new SegurancaException(e);
        } catch (InvalidKeyException e) {
            LOG.warn(e);
            throw new SegurancaException(e);
        } catch (IllegalBlockSizeException e) {
            LOG.warn(e);
            throw new SegurancaException(e);
        } catch (BadPaddingException e) {
            LOG.warn(e);
            throw new SegurancaException(e);
        }
    }

    /**
	 * Decifra uma mensagem usando a chave previamente gerada.
	 * 
	 * @param msgCifrada
	 * @return a mensagem decifrada
	 * @throws SegurancaException
	 *             quando a mensagem não pode ser decifrada
	 * @throws IOException
	 *             quando a chave não pode ser lida do arquivo
	 */
    public static String decifra(String msgCifrada) throws SegurancaException, IOException {
        if (msgCifrada == null || msgCifrada.equals("")) {
            LOG.warn("Cifrador requisitado para decifragem de String vazia ou nulla: '" + msgCifrada + "'");
            return null;
        }
        Cipher desCipher;
        try {
            desCipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
            byte[] dadoDescriptografado = null;
            dadoDescriptografado = new BASE64Decoder().decodeBuffer(msgCifrada);
            desCipher.init(Cipher.UNWRAP_MODE, getKeyGen());
            geraKeySeEstaForNula();
            desCipher.init(Cipher.DECRYPT_MODE, desCipher.unwrap(key, "DES", Cipher.SECRET_KEY));
            byte[] texto = desCipher.doFinal(dadoDescriptografado);
            return new String(texto, "UTF8");
        } catch (SegurancaException e) {
            LOG.warn(e);
            throw new SegurancaException(e);
        } catch (InvalidKeyException e) {
            LOG.warn(e);
            throw new SegurancaException(e);
        } catch (NoSuchAlgorithmException e) {
            LOG.warn(e);
            throw new SegurancaException(e);
        } catch (IllegalBlockSizeException e) {
            LOG.warn(e);
            throw new SegurancaException(e);
        } catch (BadPaddingException e) {
            LOG.warn(e);
            throw new SegurancaException(e);
        } catch (UnsupportedEncodingException e) {
            LOG.warn(e);
            throw new SegurancaException(e);
        } catch (NoSuchPaddingException e) {
            LOG.warn(e);
            throw new SegurancaException(e);
        }
    }

    private static void geraKeySeEstaForNula() throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, SegurancaException, IOException {
        if (key == null) {
            Cipher enCipher;
            enCipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
            enCipher.init(Cipher.WRAP_MODE, getKeyGen());
            key = enCipher.wrap(getKeyGen());
        }
    }

    /**
	 * Retorna a chave secreta e única de criptografia. Primeiro ela é procurada
	 * em memória, em uma propriedade da classe GeradorDeChave. Se a chave não
	 * estiver em memória, ela é resgatada do arquivo. O arquivo normalmente é
	 * definido da seguinte maneira: 1) o nome que consta no bundle do
	 * chinchilla, sob a chave app.keyfile; 2) local padrão, no diretório de
	 * execução do código com o nome "chinchilla.key".
	 * 
	 * @return a chave de criptografia
	 * @throws SegurancaException
	 * @throws IOException
	 *             quando a chave não pode ser guardada no arquivo apropriado.
	 *             Mesmo ocorrendo a exceção a chave está criada e em memória.
	 */
    private static SecretKey getKeyGen() throws SegurancaException, IOException {
        if (desKey == null) {
            try {
                desKey = readKey();
            } catch (SegurancaException e) {
                LOG.info("Arquivo com chave não foi encontrado.");
            }
        }
        if (desKey == null) {
            KeyGenerator keygen = null;
            try {
                LOG.info("Gerando nova chave");
                keygen = KeyGenerator.getInstance("DES");
                desKey = keygen.generateKey();
                writeKey(desKey);
            } catch (NoSuchAlgorithmException e) {
                LOG.warn(e);
                throw new SegurancaException(e);
            }
        }
        return desKey;
    }

    /**
	 * Escreve a chave em um arquivo descrito na URL da propriedade "chave.des"
	 * do bundle da aplicação.
	 * 
	 * @throws IOException
	 *             quando a escrita do arquivo falha
	 */
    private static void writeKey(SecretKey key) throws IOException {
        File file = new File(Bundle.msg("chave.des"));
        FileOutputStream fos = new FileOutputStream(file);
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(key);
        oos.close();
        fos.close();
        if (LOG.isInfoEnabled()) {
            LOG.info("Nova chave salva em disco: " + file);
        }
    }

    /**
	 * Lê a chave secreta necessária para criptografar os dados a partir da url
	 * descrita na propriedade "chave.des" do bundle da aplicação.
	 * 
	 * @return a chave secreta de criptografia
	 * @throws SegurancaException
	 *             quando a chave não pode ser lida por qualquer motivo.
	 */
    private static SecretKey readKey() throws SegurancaException {
        try {
            File file = new File(Bundle.msg("chave.des"));
            FileInputStream fis = new FileInputStream(file);
            ObjectInputStream ois = new ObjectInputStream(fis);
            if (LOG.isDebugEnabled()) {
                LOG.debug("Chave lida do disco: " + file);
            }
            return (SecretKey) ois.readObject();
        } catch (FileNotFoundException e) {
            LOG.warn(e);
            throw new SegurancaException(e);
        } catch (IOException e) {
            LOG.warn(e);
            throw new SegurancaException(e);
        } catch (ClassNotFoundException e) {
            LOG.warn(e);
            throw new SegurancaException(e);
        }
    }
}
