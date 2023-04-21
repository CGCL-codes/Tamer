package cz.zcu.kiv.properties_dialogs.widgets;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import org.apache.commons.logging.Log;
import org.opencms.file.CmsObject;
import org.opencms.main.CmsException;
import org.opencms.main.CmsLog;
import org.opencms.widgets.CmsSelectWidget;
import org.opencms.widgets.CmsSelectWidgetOption;
import org.opencms.widgets.I_CmsWidget;
import org.opencms.widgets.I_CmsWidgetDialog;
import org.opencms.widgets.I_CmsWidgetParameter;
import org.opencms.workplace.explorer.CmsNewResourceXmlPage;

/**
 * Web KIV modul <strong>cz.zcu.kiv.properties_dialogs</strong>
 * 
 * Copyright (c) 2007-2009 Department of Computer Science, University of West
 * Bohemia, Pilsen, CZ
 * 
 * This software and this file is available under the Creative Commons
 * Attribution-Noncommercial-Share Alike license. You may obtain a copy of the
 * License at http://creativecommons.org/licenses/ .
 * 
 * This software is provided on an "AS IS" BASIS, WITHOUT WARRANTIES OR
 * CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations.
 * 
 * This class creates a select widget for selecting one of the properties
 * defined in OpenCms
 * 
 * @author Stanislav Skalicky
 * 
 */
public class TemplatesSelectWidget extends CmsSelectWidget {

    /** The log object for this class. */
    private static final Log LOG = CmsLog.getLog(TemplatesSelectWidget.class);

    /** Suffix of the property dialog configuration file */
    private static final String CONFIG_FILE_SUFFIX = ".cfg";

    /**
	 * Creates a new select widget.
	 */
    public TemplatesSelectWidget() {
        super();
    }

    /**
	 * Creates a select widget with the select options specified in the given
	 * configuration List.
	 * 
	 * @param configuration
	 */
    @SuppressWarnings("unchecked")
    public TemplatesSelectWidget(List configuration) {
        super(configuration);
    }

    /**
	 * Creates a select widget with the specified select options.
	 * 
	 * @param configuration
	 */
    public TemplatesSelectWidget(String configuration) {
        super(configuration);
    }

    /**
	 * @see org.opencms.widgets.CmsSelectWidget#newInstance()
	 */
    public I_CmsWidget newInstance() {
        return new TemplatesSelectWidget(getConfiguration());
    }

    /**
	 * @see org.opencms.widgets.A_CmsSelectWidget#parseSelectOptions(org.opencms.file.CmsObject,
	 *      org.opencms.widgets.I_CmsWidgetDialog,
	 *      org.opencms.widgets.I_CmsWidgetParameter)
	 */
    @SuppressWarnings("unchecked")
    protected List parseSelectOptions(CmsObject cms, I_CmsWidgetDialog wd, I_CmsWidgetParameter param) {
        List result = new ArrayList();
        List<String> templates = getTemplates(cms);
        Iterator i = templates.iterator();
        while (i.hasNext()) {
            String path = (String) i.next();
            CmsSelectWidgetOption option = new CmsSelectWidgetOption(path);
            result.add(option);
        }
        setSelectOptions(result);
        return getSelectOptions();
    }

    @SuppressWarnings("unchecked")
    public List<String> getTemplates(CmsObject cms) {
        List<String> result = new ArrayList<String>();
        TreeMap<String, String> templates = null;
        try {
            templates = CmsNewResourceXmlPage.getTemplates(cms, "");
            if (templates != null) {
                Iterator i = templates.entrySet().iterator();
                while (i.hasNext()) {
                    Map.Entry entry = (Map.Entry) i.next();
                    String path = (String) entry.getValue();
                    if (!isPropertyDialogTemplate(path)) {
                        result.add(path);
                    }
                }
            }
        } catch (CmsException e) {
            if (LOG.isInfoEnabled()) {
                LOG.info(e.getLocalizedMessage());
            }
        }
        return result;
    }

    private boolean isPropertyDialogTemplate(String path) {
        boolean result = false;
        int dotIndex = path.lastIndexOf('.');
        if (dotIndex > 0) {
            if (path.substring(dotIndex, path.length()).equals(CONFIG_FILE_SUFFIX)) {
                return true;
            }
        }
        return result;
    }
}
