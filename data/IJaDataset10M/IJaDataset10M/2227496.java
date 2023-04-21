package com.doculibre.wicket.util;

import wicket.Resource;
import wicket.ResourceReference;

/**
 * Classe utilitaire qui permet de faire référence à des ressources 
 * relatives à la racine de l'application Web.
 * 
 * @author Vincent Dussault
 */
@SuppressWarnings("serial")
public class ContextPathResourceReference extends ResourceReference {

    /**
	 * Le fichier qu'on souhaite ouvrir.
	 */
    private String contextRelativePath;

    /**
	 * @param path Le chemin relatif vers la ressource (débute par un /).
	 */
    public ContextPathResourceReference(String contextRelativePath) {
        super(removeFirstSlash(contextRelativePath));
        this.contextRelativePath = contextRelativePath;
    }

    private static String removeFirstSlash(String path) {
        if (path.startsWith("/")) {
            return path.substring(1);
        } else {
            return path;
        }
    }

    /**
	 * Cet objet va lire le contenu du fichier.
	 * 
	 * @see wicket.ResourceReference#newResource()
	 */
    @Override
    protected Resource newResource() {
        return new ContextPathResource(contextRelativePath);
    }
}
