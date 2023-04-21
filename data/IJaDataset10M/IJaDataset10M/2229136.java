package org.weras.portal.clientes.client.comum.util;

/**
 * Returns the images with path
 * 
 * 
 */
public class ImgLocator {

    static final String LOCATION = "img";

    /**
	 * Returns the full location to this image name
	 * 
	 * @param name
	 * @return
	 */
    public static String locate(String name) {
        return GWTUtil.getModuleRelativePath(LOCATION) + "/" + name;
    }

    public static String novo() {
        return locate("add.gif");
    }

    public static String remover() {
        return locate("delete.gif");
    }

    public static String editar() {
        return locate("details.gif");
    }

    public static String clientes() {
        return locate("clientes.png");
    }

    public static String planos() {
        return locate("produtos.png");
    }

    public static String pagamentos() {
        return locate("pagamentos.png");
    }

    public static String funcionarios() {
        return locate("funcionarios.png");
    }

    public static String meuperfil() {
        return locate("meuperfil.png");
    }

    public static String admin() {
        return locate("admin.png");
    }

    public static String membros() {
        return locate("clientes.png");
    }
}
