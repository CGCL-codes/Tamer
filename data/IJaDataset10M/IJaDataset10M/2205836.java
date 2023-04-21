package uips.tree.convertors;

import uips.tree.convertors.exceptions.ConvertorNotInitializedException;
import uips.tree.inner.interfaces.IActionInn;
import uips.tree.inner.interfaces.IBehaviorInn;
import uips.tree.inner.interfaces.IContainerInn;
import uips.tree.inner.interfaces.IElementInn;
import uips.tree.inner.interfaces.IEventInn;
import uips.tree.inner.interfaces.IInterfaceInn;
import uips.tree.inner.interfaces.ILayoutInn;
import uips.tree.inner.interfaces.IModelInn;
import uips.tree.inner.interfaces.IPositionInn;
import uips.tree.inner.interfaces.IPropertiesInn;
import uips.tree.inner.interfaces.IPropertyInn;
import uips.tree.inner.interfaces.IStyleInn;
import uips.tree.inner.interfaces.IUIProtocolInn;
import uips.tree.inner.interfaces.IVariantInn;
import uips.tree.outer.factories.interfaces.IUIPTreeOutFactory;
import uips.tree.outer.interfaces.IActionOut;
import uips.tree.outer.interfaces.IBehaviorOut;
import uips.tree.outer.interfaces.IContainerOut;
import uips.tree.outer.interfaces.IElementOut;
import uips.tree.outer.interfaces.IEventOut;
import uips.tree.outer.interfaces.IInterfaceOut;
import uips.tree.outer.interfaces.ILayoutOut;
import uips.tree.outer.interfaces.IModelOut;
import uips.tree.outer.interfaces.IPositionOut;
import uips.tree.outer.interfaces.IPropertiesOut;
import uips.tree.outer.interfaces.IPropertyOut;
import uips.tree.outer.interfaces.IStyleOut;
import uips.tree.outer.interfaces.IUIProtocolOut;
import uips.tree.outer.interfaces.IVariantOut;

public interface IInnerToOuterConv {

    public void initializeConvertor(IUIPTreeOutFactory factoryOut);

    public IActionOut convertAction(IActionInn actionInn) throws ConvertorNotInitializedException;

    public IBehaviorOut convertBehavior(IBehaviorInn behaviorInn) throws ConvertorNotInitializedException;

    public IContainerOut convertContainer(IContainerInn containerInn) throws ConvertorNotInitializedException;

    public IElementOut convertElement(IElementInn elementInn) throws ConvertorNotInitializedException;

    public IEventOut convertEvent(IEventInn eventInn) throws ConvertorNotInitializedException;

    public IInterfaceOut convertInterface(IInterfaceInn interfaceInn) throws ConvertorNotInitializedException;

    public ILayoutOut convertLayout(ILayoutInn layoutInn) throws ConvertorNotInitializedException;

    public IModelOut convertModel(IModelInn modelInn) throws ConvertorNotInitializedException;

    public IPositionOut convertPosition(IPositionInn positionInn) throws ConvertorNotInitializedException;

    public IPropertiesOut convertProperties(IPropertiesInn propertiesInn) throws ConvertorNotInitializedException;

    public IPropertyOut convertProperty(IPropertyInn propertyInn) throws ConvertorNotInitializedException;

    public IUIProtocolOut convertUiprotocol(IUIProtocolInn uiproticolInn) throws ConvertorNotInitializedException;

    public IStyleOut convertStyle(IStyleInn styleInn) throws ConvertorNotInitializedException;

    public IVariantOut convertVariant(IVariantInn variantInn) throws ConvertorNotInitializedException;
}
