package com.daveoxley.cnery.pages;

import com.daveoxley.cbus.Application;
import com.daveoxley.seam.TablePageBean;
import java.io.Serializable;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.richfaces.model.ExtendedTableDataModel;

/**
 * <p>Page bean that corresponds to a similarly named JSP page.  This
 * class contains component definitions (and initialization code) for
 * all components that you have defined on this page, as well as
 * lifecycle methods and event handlers where you may add behavior
 * to respond to incoming events.</p>
 *
 * @author Dave Oxley <dave@daveoxley.co.uk>
 */
@Name("applications")
public class Applications extends TablePageBean<Application> implements Serializable {

    @In(value = "applicationsDataModel")
    @Override
    public void setDataModel(ExtendedTableDataModel<Application> dataModel) {
        super.setDataModel(dataModel);
    }
}
