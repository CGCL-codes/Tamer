package org.gm.jrgen.element;

import java.util.Map;
import net.sf.jasperreports.engine.JRElement;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.design.JRDesignBand;
import net.sf.jasperreports.engine.design.JRDesignElement;
import net.sf.jasperreports.engine.design.JRDesignParameter;
import net.sf.jasperreports.engine.design.JRDesignStaticText;
import net.sf.jasperreports.engine.design.JRDesignTextField;
import org.gm.jrgen.JRDesignHelper;
import org.gm.jrgen.ReflectionHelper;
import org.gm.jrgen.component.DesignComponent;
import org.gm.jrgen.exception.JrGenException;
import org.gm.jrgen.metadata.FieldMetadata;
import org.gm.jrgen.metadata.MetadataProvider;

public class ParameterJr extends ElementJr implements DesignComponent {

    public ParameterJr(String iContainerId, FieldMetadata field) {
        super(field);
        name = PARAM_ + iContainerId + "_" + field.getName();
    }

    public void fillParentBand(int columnWidth, JRDesignBand detailBand) {
        final int x = 0;
        final JRDesignStaticText staticText = getLabel();
        staticText.setX(x);
        staticText.setY(detailBand.getHeight());
        staticText.setWidth(columnWidth / 2);
        staticText.setPositionType(JRElement.POSITION_TYPE_FLOAT);
        final JRDesignElement textField = getElement();
        textField.setX(columnWidth / 2);
        textField.setY(detailBand.getHeight());
        textField.setWidth(columnWidth / 2);
        textField.setPositionType(JRElement.POSITION_TYPE_FLOAT);
        detailBand.setHeight(detailBand.getHeight() + JRDesignHelper.FONT_BOX);
        detailBand.addElement(textField);
        detailBand.addElement(staticText);
    }

    /**
	 * UserFeatures is not used
	 */
    public void fillDesign(Object iToRender, MetadataProvider iUserFeatures, Map<String, Object> parametersSource) throws JRException {
        parametersSource.put(name, getObject(iToRender));
    }

    public JRDesignParameter getDefinition() {
        final JRDesignParameter definition = new JRDesignParameter();
        definition.setName(name);
        definition.setValueClass(type);
        return definition;
    }

    /**
	 * Create the parameter text field
	 * 
	 * @param fields
	 * @return
	 */
    public JRDesignTextField getElement() {
        return JRDesignHelper.getIndirectFieldExpression(name, type, JRDesignHelper.getMarkupType(schemaFeature));
    }

    protected Object getObject(Object toRender) throws JRException {
        if (ReflectionHelper.isJRType((Class<?>) ((FieldMetadata) schemaFeature).getLanguageType())) {
            return toRender;
        } else {
            return JRDesignHelper.toString(toRender);
        }
    }

    public void saveTemplate() throws JrGenException {
    }
}
