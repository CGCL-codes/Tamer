package starcraft.gameclient.slick.views;

import starcraft.gamemodel.Planet;
import starcraft.gamemodel.Unit;
import starcraft.gamemodel.races.PlanetConnector;
import starcraft.gamemodel.ui.AbstractBaseView;
import starcraft.gamemodel.ui.AbstractOrderStackView;
import starcraft.gamemodel.ui.AbstractPlanetConnectorView;
import starcraft.gamemodel.ui.AbstractPlanetDropFieldView;
import starcraft.gamemodel.ui.AbstractPlanetSystemView;
import starcraft.gamemodel.ui.AbstractPlanetView;
import starcraft.gamemodel.ui.AbstractUnitView;
import starcraft.gamemodel.ui.IViewFactory;
import starcraft.gamemodel.ui.PlanetSystemPresenter;
import common.ui.slick.entities.SlickEntity;

public class SlickViewFactory implements IViewFactory {

    private SlickEntity content;

    public SlickViewFactory() {
    }

    @Override
    public AbstractPlanetSystemView createPlanetSystemView(PlanetSystemPresenter presenter) {
        SlickPlanetSystemView view = new SlickPlanetSystemView(presenter);
        content = view.getUIElement();
        return view;
    }

    @Override
    public AbstractPlanetView createPlanetView(Planet planet, int location, int orientation) {
        SlickPlanetView view = new SlickPlanetView(planet, location, orientation);
        content.addChild(view.getUIElement());
        return view;
    }

    @Override
    public void destroyPlanetView(AbstractPlanetView view) {
        content.removeChild(((SlickViewElement) view).getUIElement());
    }

    @Override
    public AbstractPlanetDropFieldView createPlanetDropFieldView(Integer field) {
        SlickPlanetDropFieldView view = new SlickPlanetDropFieldView(field);
        content.addChild(view.getUIElement());
        return view;
    }

    @Override
    public void destroyPlanetDropFieldView(AbstractPlanetDropFieldView view) {
        content.removeChild(((SlickViewElement) view).getUIElement());
    }

    @Override
    public AbstractPlanetConnectorView createPlanetConnectorView(PlanetConnector connector) {
        SlickPlanetConnectorView view = new SlickPlanetConnectorView(connector);
        content.addChild(view.getUIElement());
        return view;
    }

    @Override
    public void destroyPlanetConnectorView(AbstractPlanetConnectorView view) {
        content.removeChild(((SlickViewElement) view).getUIElement());
    }

    @Override
    public AbstractBaseView createBaseView(starcraft.gamemodel.ui.BasePlacement basePlacement) {
        SlickBaseView view = new SlickBaseView(basePlacement);
        content.addChild(view.getUIElement());
        return view;
    }

    @Override
    public void destroyBaseView(AbstractBaseView view) {
        content.removeChild(((SlickViewElement) view).getUIElement());
    }

    @Override
    public AbstractUnitView createUnitView(Unit unitPlacement) {
        SlickUnitView view = new SlickUnitView(unitPlacement);
        content.addChild(view.getUIElement());
        return view;
    }

    @Override
    public void destroyUnitView(AbstractUnitView view) {
        content.removeChild(((SlickViewElement) view).getUIElement());
    }

    @Override
    public AbstractOrderStackView createOrderStackView(Planet planet) {
        SlickOrderStackView view = new SlickOrderStackView(planet);
        content.addChild(view.getUIElement());
        return view;
    }

    @Override
    public void destroyOrderStackView(AbstractOrderStackView view) {
        content.removeChild(((SlickViewElement) view).getUIElement());
    }
}
