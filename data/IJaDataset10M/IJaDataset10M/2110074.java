package model;

import model.vos.Articulo;

public class ArticuloConcreteBuilder extends AbstractBuilder {

    protected Articulo builderProduct;

    @Override
    public void bluilPart() {
        builderProduct = new Articulo();
    }

    @Override
    public ArticuloComponente getBluilPart() {
        return builderProduct;
    }
}
