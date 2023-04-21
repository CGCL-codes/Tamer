package br.com.rbarbioni.http.async;

import java.io.BufferedInputStream;
import java.net.HttpURLConnection;

/**
 * Delegate que controla as chamadas a rotina de cache dos recurso que s�o baixados da internet.    
 **/
public interface URLCacheControllerDelegate {

    /**
	 * Chamada de falha do download do recurso. 
	 * @param HttpURLConnection que representa a conex�o a URL requisitando o download do recurso.
	 **/
    public void connectionDidFail(HttpURLConnection connection);

    /**
	 * Chamada de sucesso do download do recurso.
	 * @param filePath Caminho da imagem que foi salvo o recurso em disco.
	 * 
	 **/
    public void connectionDidFinish(String filePath);

    /**
	 * Chamada de sucesso da grava?�o do recurso em disco.
	 * @param theBufferedInputStream Stream bin�rio que representa a imagem salva em disco.
	 **/
    public void fileReady(BufferedInputStream theBufferedInputStream);
}
