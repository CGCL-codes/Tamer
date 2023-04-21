package org.rapla.mobile.android.widget;

import org.rapla.entities.dynamictype.Attribute;
import org.rapla.entities.dynamictype.AttributeType;
import android.content.Context;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * This class wraps a rapla dynamic type attribute of type boolean and provides
 * the necessary interface for being a list item.
 * 
 * @author Maximilian Lenkeit <dev@lenki.com>
 * 
 */
public class RaplaAttributeBoolean extends RaplaAttribute<Boolean> {

    private CheckBox widget;

    /**
	 * @param context
	 *            Current context
	 * @param attribute
	 *            Rapla attribute
	 */
    public RaplaAttributeBoolean(Context context, Attribute attribute) {
        super(context, attribute, AttributeType.BOOLEAN);
    }

    /**
	 * @see org.rapla.mobile.android.widget.RaplaAttribute#getListItemView()
	 */
    @Override
    public View getListItemView() {
        RelativeLayout layout = new RelativeLayout(this.getContext());
        TextView label = this.getLabel();
        layout.addView(label, 0);
        this.widget = new CheckBox(this.getContext());
        this.setValueToWidget(this.value);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        layout.addView(this.widget, 1, params);
        return layout;
    }

    /**
	 * @see org.rapla.mobile.android.widget.RaplaAttribute#setValueToWidget()
	 */
    @Override
    protected void setValueToWidget(Boolean value) {
        if (this.widget != null) {
            this.widget.setChecked(value.equals(Boolean.TRUE));
        }
    }

    /**
	 * @see org.rapla.mobile.android.widget.RaplaAttribute#getValueFromWidget()
	 */
    @Override
    protected Boolean getValueFromWidget() {
        if (this.widget == null) {
            return this.value;
        } else {
            return this.widget.isChecked() ? Boolean.TRUE : Boolean.FALSE;
        }
    }
}
