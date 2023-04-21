package uk.ac.leeds.comp.ui.factory.guice;

import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import uk.ac.leeds.comp.ui.base.UIModel;
import uk.ac.leeds.comp.ui.base.UIView;
import uk.ac.leeds.comp.ui.itemlist.ItemListModel;
import uk.ac.leeds.comp.ui.itemlist.item.ItemModel;
import uk.ac.leeds.comp.ui.selection.UISelectionModel;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.google.inject.TypeLiteral;
import com.google.inject.util.Types;

/**
 * Provides utility methods for defining common bindings necessary for the leeds
 * mvc framework.
 * 
 * @author rdenaux
 * 
 */
public class MVCGuiceUtil {

    public static <SelectedModelType extends UIModel> TypeLiteral<UISelectionModel<SelectedModelType>> getSelectionModelTypedLiteral(Class<SelectedModelType> selectedModelClass) {
        ParameterizedType fType = Types.newParameterizedType(UISelectionModel.class, selectedModelClass);
        @SuppressWarnings("unchecked") TypeLiteral<UISelectionModel<SelectedModelType>> typeLiteral = (TypeLiteral<UISelectionModel<SelectedModelType>>) TypeLiteral.get(fType);
        return typeLiteral;
    }

    public static <ItemModelType extends ItemModel<?>> TypeLiteral<ItemListModel<ItemModelType>> getItemListModelTypedLiteral(Class<ItemModelType> itemModelClass) {
        ParameterizedType fType = Types.newParameterizedType(ItemListModel.class, itemModelClass);
        @SuppressWarnings("unchecked") TypeLiteral<ItemListModel<ItemModelType>> typeLiteral = (TypeLiteral<ItemListModel<ItemModelType>>) TypeLiteral.get(fType);
        return typeLiteral;
    }

    public static <PresentedDataType extends Object> TypeLiteral<UIView<PresentedDataType>> getUIViewTypeLiteral(Class<PresentedDataType> presentedDataClass) {
        ParameterizedType fType = Types.newParameterizedType(UIView.class, presentedDataClass);
        @SuppressWarnings("unchecked") TypeLiteral<UIView<PresentedDataType>> typeLiteral = (TypeLiteral<UIView<PresentedDataType>>) TypeLiteral.get(fType);
        return typeLiteral;
    }

    public static Injector createInjectorWithMvcFactories(Module... modules) {
        List<Module> mods = new ArrayList<Module>();
        mods.addAll(Arrays.asList(modules));
        mods.add(new GuiceUIModelFactoryModule());
        mods.add(new GuiceUIControllerFactoryModule());
        Injector result = Guice.createInjector(mods);
        GuiceUIModelFactoryModule.setUpUIModelFactory(result);
        GuiceUIControllerFactoryModule.setUpUIControllerFactory(result);
        return result;
    }
}
