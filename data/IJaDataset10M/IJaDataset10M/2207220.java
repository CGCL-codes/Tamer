package org.az.gsm.web.client.managed.ui;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style.Display;
import com.google.gwt.editor.client.EditorError;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.requestfactory.client.RequestFactoryEditorDriver;
import com.google.gwt.requestfactory.shared.RequestFactory;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.text.shared.AbstractRenderer;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DoubleBox;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.IntegerBox;
import com.google.gwt.user.client.ui.LongBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.ValueListBox;
import com.google.gwt.user.datepicker.client.DateBox;
import java.util.Collection;
import java.util.List;
import org.az.gsm.common.client.model.SkillRefProxy;
import org.az.gsm.web.client.managed.activity.SkillRefEditActivityWrapper;
import org.az.gsm.web.client.managed.activity.SkillRefEditActivityWrapper.View;
import org.az.gsm.web.client.scaffold.place.ProxyEditView;
import org.az.gsm.web.client.scaffold.ui.*;

public abstract class SkillRefEditView_Roo_Gwt extends Composite implements View<SkillRefEditView> {

    @UiField
    LongBox parentId;

    @UiField
    LongBox childId;

    @UiField
    FloatBox weight;

    @UiField
    IntegerBox type;
}
