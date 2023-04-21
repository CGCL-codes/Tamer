package org.spantus.work.ui.dto;

import java.io.Serializable;

/**
 *
 * @author mondhs
 */
public class RecognitionConfig implements Serializable {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    private Integer radius;

    private String dtwWindow;

    private String repositoryPath;

    public String getDtwWindow() {
        return dtwWindow;
    }

    public void setDtwWindow(String dtwWindow) {
        this.dtwWindow = dtwWindow;
    }

    public Integer getRadius() {
        return radius;
    }

    public void setRadius(Integer radius) {
        this.radius = radius;
    }

    public String getRepositoryPath() {
        return repositoryPath;
    }

    public void setRepositoryPath(String repositoryPath) {
        this.repositoryPath = repositoryPath;
    }
}
