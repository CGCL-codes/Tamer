package br.com.linkcom.neo.view.template;

/**
 * @author rogelgarcia
 * @since 03/02/2006
 * @version 1.1
 */
public class FiltroTag extends TemplateTag {

    protected String name = "filtro";

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    protected void doComponent() throws Exception {
        includeJspTemplate();
    }
}
