package org.iqual.chaplin.example.supervisingController.withProtocol;

import org.iqual.chaplin.FromContext;
import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * User: zslajchrt
 * Date: Jan 3, 2010
 * Time: 4:52:51 PM
 * To change this template use File | Settings | File Templates.
 */
public interface Protocol {

    @FromContext(role = ActualField.class)
    String actualTextValue();

    @FromContext(role = VarianceField.class)
    void varianceColor(Color color);

    @FromContext(role = Reading.class)
    int varianceValue();

    @FromContext(role = Reading.class)
    void actualValue(int value);

    @FromContext(role = Reading.class)
    int varianceCategory();

    void onModelChanged();

    void actualFieldChanged();
}
