package br.unisinos.cs.gp.image.handler;

import java.io.File;
import java.awt.image.BufferedImage;

/**
 * Interface para Manipulador de Imagens
 * 
 * @author Wanderson Henrique Camargo Rosa
 */
public interface ImageHandler {

    public BufferedImage read(File file) throws ImageHandlerException;
}
