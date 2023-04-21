package com.mclub.client.forms.incoming;

import java.util.List;
import com.extjs.gxt.ui.client.Style.Orientation;
import com.extjs.gxt.ui.client.data.BeanModel;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionChangedEvent;
import com.extjs.gxt.ui.client.event.SelectionChangedListener;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.util.Margins;
import com.extjs.gxt.ui.client.util.Size;
import com.extjs.gxt.ui.client.widget.CheckBoxListView;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.Label;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.ComboBox;
import com.extjs.gxt.ui.client.widget.form.Field;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.layout.RowData;
import com.extjs.gxt.ui.client.widget.layout.RowLayout;
import com.gmvc.client.base.AbstractEditor;
import com.gmvc.client.base.IController;
import com.gmvc.client.meta.Event;
import com.gmvc.client.util.Tags;
import com.gmvc.client.util.Utils;
import com.google.gwt.user.client.ui.AbstractImagePrototype;
import com.mclub.client.app.SpecialEvent;
import com.mclub.client.image.Images;
import com.mclub.client.model.CustomerDTO;
import com.mclub.client.model.IncomingMovieDTO;
import com.mclub.client.model.IncomingMovieDetailDTO;
import com.mclub.client.util.GUIHelper;

/**
 * Gelen filmler, Editor sinifi
 * 
 * @see AbstractEditor
 * 
 * @author mdpinar
 * 
 */
public class IncomingMovieEditor extends AbstractEditor<IncomingMovieDTO> {

    /**
	 * Musterinin elinde bulunan tum filmleri secilebilsin diye 
	 * listeleyen check li liste kutusu, kontrolor den erisilsin diye protected yapildi
	 * 
	 */
    protected CheckBoxListView<BeanModel> cbListView;

    /**
	 * Ekli filmleri yoneten mini kontrolor
	 * 
	 */
    private IController<IncomingMovieDetailDTO> miniController;

    /**
	 * Musteri secim combosu
	 * 
	 */
    private ComboBox<BeanModel> customerCombo;

    /**
	 * Aciklama alani
	 */
    private TextField<String> descText;

    public IncomingMovieEditor(IController<IncomingMovieDTO> controller) {
        super(controller);
    }

    /**
	 * {@inheritDoc}
	 */
    @Override
    public IncomingMovieDTO getNewModel() {
        return new IncomingMovieDTO();
    }

    /**
	 * {@inheritDoc}
	 */
    @Override
    public Size getSize() {
        return new Size(410, 450);
    }

    /**
	 * {@inheritDoc}
	 */
    @Override
    public String isValid() {
        StringBuilder sb = new StringBuilder();
        if (customerCombo.getValue() == null) sb.append(Tags.get("customerCanNotBeEmpty"));
        return sb.toString();
    }

    /**
	 * {@inheritDoc}
	 */
    @Override
    public String getTitle() {
        return Tags.get("incomingMovies");
    }

    /**
	 * {@inheritDoc}
	 */
    @Override
    public Field<?> getFirstField() {
        return customerCombo;
    }

    /**
	 * {@inheritDoc}
	 */
    @Override
    public void pushFields() {
        model.setCustomer((CustomerDTO) customerCombo.getValue().getBean());
        model.setDescription(descText.getValue());
        miniController.fireEvent(new Event(SpecialEvent.MiniDoUpdates));
    }

    /**
	 * {@inheritDoc}
	 */
    @Override
    public void popFields() {
        customerCombo.setEditable(model.getId() == null);
        if (model.getCustomer() != null) customerCombo.setValue(Utils.convertToBeanModel(model.getCustomer())); else customerCombo.setValue(null);
        descText.setValue(model.getDescription());
        cbListView.setStore(new ListStore<BeanModel>());
        miniController.fireEvent(new Event(SpecialEvent.MiniSetModels).addParam("modelList", model.getDetails()));
    }

    /**
	 * {@inheritDoc}
	 */
    @Override
    public void init() {
        miniController = new IncomingMovieMiniController(getController());
        customerCombo = GUIHelper.getCustomerSuggestCombo();
        customerCombo.addSelectionChangedListener(new SelectionChangedListener<BeanModel>() {

            @Override
            public void selectionChanged(SelectionChangedEvent<BeanModel> se) {
                if (se != null && se.getSelectedItem() != null) {
                    CustomerDTO customer = se.getSelectedItem().getBean();
                    getController().fireEvent(new Event(SpecialEvent.LoadMovie, customer));
                    miniController.fireEvent(new Event(SpecialEvent.MiniSetModels));
                }
            }
        });
        addFieldToForm(customerCombo);
        descText = new TextField<String>();
        descText.setFieldLabel(Tags.get("description"));
        descText.setName("desc");
        addFieldToForm(descText);
        addWidgetToForm(buildChoosingMoviePanel());
        getMainPanel().setLayout(new RowLayout(Orientation.VERTICAL));
        addWidgetToPanel(getFormPanel(), new RowData(1, -1, new Margins(4)));
        addWidgetToPanel(miniController.getBrowser().getGrid(), new RowData(1, 1, new Margins(0, 4, 0, 4)));
    }

    /**
	 * Film secme listesi panelini bina eder
	 * 
	 */
    private ContentPanel buildChoosingMoviePanel() {
        ContentPanel panel = new ContentPanel(new RowLayout(Orientation.VERTICAL));
        panel.setHeaderVisible(false);
        panel.setHeight(170);
        panel.setFrame(false);
        cbListView = new CheckBoxListView<BeanModel>();
        cbListView.setDisplayProperty("movieName");
        panel.add(new Label(Tags.get("customersSelectableMovies")), new RowData(1, -1, new Margins(1)));
        panel.add(cbListView, new RowData(1, 1, new Margins(4, 1, 0, 1)));
        panel.addButton(new Button(Tags.get("addSelectedMoviesToGrid"), AbstractImagePrototype.create(Images.Instance.down()), new SelectionListener<ButtonEvent>() {

            @Override
            public void componentSelected(ButtonEvent ce) {
                List<BeanModel> beanList = cbListView.getChecked();
                for (BeanModel bm : beanList) {
                    IncomingMovieDetailDTO incomingDetail = (IncomingMovieDetailDTO) bm.getBean();
                    incomingDetail.setIncomingMovie(model);
                    miniController.fireEvent(new Event(SpecialEvent.AddNewModel, incomingDetail));
                    cbListView.getStore().remove(bm);
                }
                miniController.fireEvent(new Event(SpecialEvent.Refresh));
            }
        }));
        return panel;
    }
}
