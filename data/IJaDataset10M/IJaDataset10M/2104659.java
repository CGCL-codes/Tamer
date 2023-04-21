package com.gwtaf.ext.core.client.field;

import com.google.gwt.user.client.Timer;
import com.gwtaf.core.client.editcontext.EditContextListenerAdapter;
import com.gwtaf.core.client.editcontext.IControllerContext;
import com.gwtaf.core.client.editcontext.IEditStateProvider;
import com.gwtaf.core.client.fieldadapter.IFieldAdapter;
import com.gwtext.client.data.Record;
import com.gwtext.client.widgets.Panel;
import com.gwtext.client.widgets.form.ComboBox;
import com.gwtext.client.widgets.form.Field;
import com.gwtext.client.widgets.form.FieldSet;
import com.gwtext.client.widgets.form.FormPanel;
import com.gwtext.client.widgets.form.MultiFieldPanel;
import com.gwtext.client.widgets.form.event.ComboBoxListenerAdapter;
import com.gwtext.client.widgets.form.event.FieldListenerAdapter;
import com.gwtext.client.widgets.layout.AnchorLayoutData;
import com.gwtext.client.widgets.layout.ColumnLayoutData;
import com.gwtext.client.widgets.layout.LayoutData;

public class FormAdapter implements IEditStateProvider {

    private FormPanel form;

    private IControllerContext ctx;

    private boolean dirty;

    public FormAdapter(FormPanel formPanel, IControllerContext ctx) {
        this.form = formPanel;
        this.ctx = ctx;
        this.ctx.addStateProvider(this);
        this.ctx.addStateListener(new EditContextListenerAdapter() {

            @Override
            public void onSetData() {
                form.doLayout();
                dirty = false;
            }
        });
    }

    public FormPanel getForm() {
        return form;
    }

    public void add(IFieldAdapter<?> fieldAdp) {
        form.add(fieldAdp.getWidget());
        listenForChange(fieldAdp);
    }

    public void add(IFieldAdapter<?> fieldAdp, LayoutData ld) {
        form.add(fieldAdp.getWidget(), ld);
        listenForChange(fieldAdp);
    }

    private void listenForChange(final IFieldAdapter<?> fieldAdp) {
        if (fieldAdp.getWidget() instanceof Field) {
            ((Field) fieldAdp.getWidget()).addListener(new FieldListenerAdapter() {

                @Override
                public void onChange(Field field, Object newVal, Object oldVal) {
                    dirty = true;
                    ctx.updateState();
                }
            });
        }
        if (fieldAdp.getWidget() instanceof ComboBox) {
            ((ComboBox) fieldAdp.getWidget()).addListener(new ComboBoxListenerAdapter() {

                @Override
                public void onSelect(ComboBox comboBox, Record record, int index) {
                    new Timer() {

                        @Override
                        public void run() {
                            dirty = true;
                            ctx.updateState();
                        }
                    }.schedule(100);
                }
            });
        }
    }

    public class MultiFieldAdapter {

        private MultiFieldPanel panel;

        public MultiFieldAdapter() {
            this.panel = new MultiFieldPanel();
            this.panel.setBorder(false);
            form.add(this.panel);
        }

        public MultiFieldAdapter(FieldSetAdapter fa) {
            this.panel = new MultiFieldPanel();
            this.panel.setBorder(false);
            fa.getPanel().add(this.panel);
        }

        public void addToRow(IFieldAdapter<?> fieldAdp, ColumnLayoutData data) {
            if (fieldAdp.getWidget() instanceof Field) panel.addToRow((Field) fieldAdp.getWidget(), data); else panel.addToRow(fieldAdp.getWidget(), data);
            listenForChange(fieldAdp);
        }

        public void addToRow(IFieldAdapter<?> fieldAdp, int width) {
            if (fieldAdp.getWidget() instanceof Field) panel.addToRow((Field) fieldAdp.getWidget(), width); else panel.addToRow(fieldAdp.getWidget(), width);
            listenForChange(fieldAdp);
        }

        public void addToRow(Panel field, int width) {
            panel.addToRow(field, width);
        }

        public MultiFieldPanel getPanel() {
            return panel;
        }
    }

    public class FieldSetAdapter {

        private FieldSet panel;

        public FieldSetAdapter() {
            this.panel = new FieldSet();
            this.panel.setBorder(false);
            form.add(this.panel);
        }

        public FieldSetAdapter(String title) {
            this.panel = new FieldSet(title);
            this.panel.setBorder(false);
            form.add(this.panel);
        }

        public void add(IFieldAdapter<?> fieldAdp) {
            panel.add(fieldAdp.getWidget());
            listenForChange(fieldAdp);
        }

        public void add(IFieldAdapter<?> fieldAdp, AnchorLayoutData data) {
            panel.add(fieldAdp.getWidget(), data);
            listenForChange(fieldAdp);
        }

        public FieldSet getPanel() {
            return panel;
        }
    }

    public boolean isDirty() {
        return this.dirty;
    }

    public boolean isValid() {
        return form.getForm().isValid();
    }

    @Deprecated
    public void setDirty() {
        if (dirty == false) {
            dirty = true;
            ctx.updateState();
        }
    }
}
