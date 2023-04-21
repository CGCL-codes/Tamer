package cn.ekuma.epos.datalogic.update;

import cn.ekuma.epos.datalogic.I_DataLogicSystem;
import cn.ekuma.epos.datalogic.adempiere.Adempiere;
import com.openbravo.data.basic.BasicException;
import com.openbravo.data.gui.JMessageDialog;
import com.openbravo.data.gui.MessageInf;
import com.openbravo.data.loader.BatchSentence;
import com.openbravo.data.loader.BatchSentenceResource;
import com.openbravo.data.loader.Session;
import com.openbravo.pos.base.AppConfig;
import com.openbravo.pos.base.AppLocal;
import com.openbravo.pos.base.AppProperties;
import com.openbravo.pos.base.AppView;
import com.openbravo.pos.base.AppViewConnection;
import com.openbravo.pos.base.BeanFactory;
import com.openbravo.pos.base.BeanFactoryException;
import com.openbravo.pos.base.BeanFactoryObj;
import com.openbravo.pos.base.BeanFactoryScript;
import com.openbravo.pos.instance.InstanceQuery;
import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import javax.swing.JOptionPane;

/**
 *
 * @author Administrator
 */
public class Update230 {

    private Session session;

    private I_DataLogicSystem m_dlSystem;

    private Map<String, BeanFactory> m_aBeanFactories;

    private static HashMap<String, String> m_oldclasses;

    public Update230() {
        m_aBeanFactories = new HashMap<String, BeanFactory>();
    }

    public boolean update(AppProperties m_props) {
        if (m_props.getProperty("datalogictype").equalsIgnoreCase("adempiere")) {
            m_oldclasses.put("com.openbravo.pos.forms.I_DataLogicSystem", "cn.ekuma.epos.datalogic.adempiere.AdempiereDataLogicSystem");
            m_oldclasses.put("com.openbravo.pos.forms.I_DataLogicSales", "cn.ekuma.epos.datalogic.adempiere.AdempiereDataLogicSales");
            m_oldclasses.put("com.openbravo.pos.sales.I_DataLogicReceipts", "cn.ekuma.epos.datalogic.adempiere.AdempiereDataLogicReceipts");
            m_oldclasses.put("com.openbravo.pos.customers.I_DataLogicCustomers", "cn.ekuma.epos.datalogic.adempiere.AdempiereDataLogicCustomers");
            m_oldclasses.put("com.openbravo.pos.admin.I_DataLogicAdmin", "cn.ekuma.epos.datalogic.adempiere.AdempiereDataLogicAdmin");
        }
        try {
            session = new AppViewConnection().createSession(m_props);
        } catch (BasicException e) {
            JMessageDialog.showMessage(new java.awt.Frame(), new MessageInf(MessageInf.SGN_DANGER, e.getMessage(), e));
            return false;
        }
        m_dlSystem = (I_DataLogicSystem) getBean("com.openbravo.pos.forms.I_DataLogicSystem");
        m_dlSystem.init(session, null);
        String sDBVersion = readDataBaseVersion();
        if (sDBVersion == null || !AppLocal.APP_VERSION.equalsIgnoreCase(sDBVersion)) {
            JMessageDialog.showMessage(new java.awt.Frame(), new MessageInf(MessageInf.SGN_DANGER, AppLocal.getIntString("message.appversionnosuppot")));
            session.close();
            return false;
        }
        String sScript = sDBVersion == null ? m_dlSystem.getInitScript() + "-create.sql" : m_dlSystem.getInitScript() + "-epos-upgrade-2.30.sql";
        if (Update230.class.getResource(sScript) == null) {
            JMessageDialog.showMessage(new java.awt.Frame(), new MessageInf(MessageInf.SGN_DANGER, sDBVersion == null ? AppLocal.getIntString("message.databasenotsupported", session.DB.getName()) : AppLocal.getIntString("message.noupdatescript")));
            session.close();
            return false;
        } else {
            if (JOptionPane.showConfirmDialog(new java.awt.Frame(), AppLocal.getIntString(sDBVersion == null ? "message.createdatabase" : "message.updatedatabase") + "<--" + sScript, AppLocal.getIntString("message.title"), JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION) {
                try {
                    BatchSentence bsentence = new BatchSentenceResource(session, sScript);
                    bsentence.putParameter("APP_ID", Matcher.quoteReplacement(AppLocal.APP_ID));
                    bsentence.putParameter("APP_NAME", Matcher.quoteReplacement(AppLocal.APP_NAME));
                    bsentence.putParameter("APP_VERSION", Matcher.quoteReplacement(AppLocal.APP_VERSION));
                    bsentence.putParameter("AD_CLIENT_ID", Matcher.quoteReplacement(AppConfig.getAppProperty(Adempiere.AD_CLIENT_ID)));
                    bsentence.putParameter("AD_ORG_ID", Matcher.quoteReplacement(AppConfig.getAppProperty(Adempiere.AD_ORG_ID)));
                    java.util.List l = bsentence.list();
                    if (l.size() > 0) {
                        JMessageDialog.showMessage(new java.awt.Frame(), new MessageInf(MessageInf.SGN_WARNING, AppLocal.getIntString("Database.ScriptWarning"), l.toArray(new Throwable[l.size()])));
                    }
                } catch (BasicException e) {
                    JMessageDialog.showMessage(new java.awt.Frame(), new MessageInf(MessageInf.SGN_DANGER, AppLocal.getIntString("Database.ScriptError"), e));
                    session.close();
                    return false;
                }
            } else {
                session.close();
                return false;
            }
        }
        return true;
    }

    private String readDataBaseVersion() {
        try {
            return m_dlSystem.findVersion();
        } catch (BasicException ed) {
            return null;
        }
    }

    public static void main(final String args[]) {
        if (!registerApp()) {
            System.exit(1);
        }
        initOldClasses();
        AppConfig config = new AppConfig(args);
        config.load();
        String slang = config.getProperty("user.language");
        String scountry = config.getProperty("user.country");
        String svariant = config.getProperty("user.variant");
        if (slang != null && !slang.equals("") && scountry != null && svariant != null) {
            Locale.setDefault(new Locale(slang, scountry, svariant));
        }
        new Update230().update(config);
        System.exit(1);
    }

    public static boolean registerApp() {
        InstanceQuery i = null;
        try {
            i = new InstanceQuery();
            i.getAppMessage().restoreWindow();
            return false;
        } catch (Exception e) {
            return true;
        }
    }

    public Object getBean(String beanfactory) throws BeanFactoryException {
        beanfactory = mapNewClass(beanfactory);
        BeanFactory bf = m_aBeanFactories.get(beanfactory);
        if (bf == null) {
            if (beanfactory.startsWith("/")) {
                bf = new BeanFactoryScript(beanfactory);
            } else {
                try {
                    Class bfclass = Class.forName(beanfactory);
                    if (BeanFactory.class.isAssignableFrom(bfclass)) {
                        bf = (BeanFactory) bfclass.newInstance();
                    } else {
                        Constructor constMyView = bfclass.getConstructor(new Class[] { AppView.class });
                        Object bean = constMyView.newInstance(new Object[] { this });
                        bf = new BeanFactoryObj(bean);
                    }
                } catch (Exception e) {
                    throw new BeanFactoryException(e);
                }
            }
            m_aBeanFactories.put(beanfactory, bf);
        }
        return bf.getBean();
    }

    private static String mapNewClass(String classname) {
        String newclass = m_oldclasses.get(classname);
        return newclass == null ? classname : newclass;
    }

    private static void initOldClasses() {
        m_oldclasses = new HashMap<String, String>();
        m_oldclasses.put("com.openbravo.pos.forms.I_DataLogicSystem", "cn.ekuma.epos.datalogic.define.DataLogicSystem");
        m_oldclasses.put("com.openbravo.pos.forms.I_DataLogicSales", "cn.ekuma.epos.datalogic.define.DataLogicSales");
        m_oldclasses.put("com.openbravo.pos.sales.I_DataLogicReceipts", "cn.ekuma.epos.datalogic.define.DataLogicReceipts");
        m_oldclasses.put("com.openbravo.pos.customers.I_DataLogicCustomers", "cn.ekuma.epos.datalogic.define.DataLogicCustomers");
        m_oldclasses.put("com.openbravo.pos.admin.I_DataLogicAdmin", "cn.ekuma.epos.datalogic.define.DataLogicAdmin");
    }
}
