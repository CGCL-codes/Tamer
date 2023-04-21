package cx.ath.contribs.webFrame.controller;

import java.lang.reflect.Constructor;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import cx.ath.contribs.attributedTree.xml.generalApplication.controller.GeneralController;
import cx.ath.contribs.attributedTree.xml.transform.ExtTransformer;
import cx.ath.contribs.attributedTree.xml.util.TransformationLogMessages;
import cx.ath.contribs.internal.xerces.dom.NodeImpl;
import cx.ath.contribs.internal.xerces.jaxp.DocumentBuilderImpl;
import cx.ath.contribs.standardApp.attributedTree.businessLogic.StandardBusinessLogic;
import cx.ath.contribs.standardApp.attributedTree.controller.StandardController;
import cx.ath.contribs.util.maintenance.Configuration;
import cx.ath.contribs.webFrame.businessLogic.WebBusinessLogicEnvironment;
import cx.ath.contribs.webFrame.ui.WFUi;
import cx.ath.contribs.webFrame.ui.WFUiEnvironment;

public class ServletController<E extends ServletControllerEnvironment> extends StandardController<E> {

    protected StandardBusinessLogic<WebBusinessLogicEnvironment> _logic;

    protected WFUi<WFUiEnvironment> _ui;

    public ServletController(E env) {
        super(env);
        _logic = getBusinessLogic();
        _ui = getUi();
    }

    public StandardBusinessLogic getBusinessLogic() {
        if (_logic != null) return _logic;
        try {
            Class classWD = Class.forName(_env.getConfiguration().getProperty("BusinessLogicEnvironment"));
            Class[] formParamWD = new Class[4];
            formParamWD[0] = Configuration.class;
            formParamWD[1] = TransformationLogMessages.class;
            formParamWD[2] = DocumentBuilderImpl.class;
            formParamWD[3] = StorageManager.class;
            Constructor constructorWD = classWD.getConstructor(formParamWD);
            Object[] actParamWD = new Object[4];
            actParamWD[0] = _env.getConfiguration();
            actParamWD[1] = _env.getLog();
            actParamWD[2] = _env.getDocumentBuilder();
            actParamWD[3] = _env.getStorageManager();
            _logic = new StandardBusinessLogic(((WebBusinessLogicEnvironment) constructorWD.newInstance(actParamWD)));
            return _logic;
        } catch (Exception ex) {
            _env.getLog().write("", ex);
            return null;
        }
    }

    public WFUi getUi() {
        if (_ui != null) return (WFUi) _ui;
        WFUiEnvironment uiEnv = null;
        try {
            if (uiEnv == null) {
                Class classWD = Class.forName(_env.getConfiguration().getProperty("UiEnvironment"));
                Class[] formParamWD = new Class[4];
                formParamWD[0] = Configuration.class;
                formParamWD[1] = TransformationLogMessages.class;
                formParamWD[2] = DocumentBuilderImpl.class;
                formParamWD[3] = StorageManager.class;
                Constructor constructorWD = classWD.getConstructor(formParamWD);
                Object[] actParamWD = new Object[4];
                actParamWD[0] = _env.getConfiguration();
                actParamWD[1] = _env.getLog();
                actParamWD[2] = _env.getDocumentBuilder();
                actParamWD[3] = _env.getStorageManager();
                uiEnv = (WFUiEnvironment) constructorWD.newInstance(actParamWD);
            }
            if (_ui == null) {
                Class classWD = Class.forName(_env.getConfiguration().getProperty("Ui"));
                Class[] formParamWD = new Class[1];
                formParamWD[0] = WFUiEnvironment.class;
                Constructor constructorWD = classWD.getConstructor(formParamWD);
                Object[] actParamWD = new Object[1];
                actParamWD[0] = uiEnv;
                _ui = (WFUi) constructorWD.newInstance(actParamWD);
            }
            return (WFUi) _ui;
        } catch (Exception ex) {
            _env.getLog().write("Exception in StandardControllerEnvironment/getUi", ex);
            return null;
        }
    }

    public ExtTransformer<E, GeneralController<E>> newTransformer() throws TransformerConfigurationException {
        _ui.getEnvironment().setDialogData(_env.getRequest(), _env.getResponse());
        return new ExtTransformer<E, GeneralController<E>>(this) {

            public NodeImpl transform(NodeImpl node) throws TransformerConfigurationException, TransformerException {
                return _ui._t_(_logic._t_(_ui.buildRequestNode()));
            }
        };
    }
}
