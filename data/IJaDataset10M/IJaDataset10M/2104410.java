package gemini.pollux.ui.client.page.content.product.producttype.widget;

import com.google.gwt.event.shared.EventHandler;

public interface ProductTypeTableHandler extends EventHandler {

    void onAdd(ProductTypeTableEvent event);

    void onDelete(ProductTypeTableEvent event);

    void onEdit(ProductTypeTableEvent event);
}
