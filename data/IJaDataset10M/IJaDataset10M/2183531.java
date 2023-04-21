package br.com.caelum.testslicer.ant;

import java.util.List;

/**
 * Discover changes files (somehow).
 * 
 * @author Guilherme Silveira
 */
public interface ChangedFilesSelector {

    List<String> discoverChangedFiles();

    void updateChangedFiles();
}
