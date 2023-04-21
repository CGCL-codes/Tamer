package com.whitebearsolutions.imagine.wbsagnitio.idm;

import java.io.File;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import com.whitebearsolutions.imagine.wbsagnitio.NetworkManager;
import com.whitebearsolutions.imagine.wbsagnitio.configuration.TomcatConfiguration;
import com.whitebearsolutions.imagine.wbsagnitio.configuration.WBSAgnitioConfiguration;
import com.whitebearsolutions.imagine.wbsagnitio.directory.AttributeSet;
import com.whitebearsolutions.imagine.wbsagnitio.net.CommResponse;
import com.whitebearsolutions.imagine.wbsagnitio.transport.CustomTransport;
import com.whitebearsolutions.io.FileLock;

public class RuleDecisionPointTransaction {

    public static final int TRANSACTION_IN_PROCESS = 1;

    public static final int TRANSACTION_CONFIRMED = 2;

    public static final int TRANSACTION_REJECTED = 3;

    public static final int CONFIRMATION_NOT_ANSWERED = 1;

    public static final int CONFIRMATION_CONFIRMED = 2;

    public static final int CONFIRMATION_REJECTED = 3;

    private String _uuid;

    private static char[] VALID_CHARS;

    private static Map<Integer, String> HTML_CHARS;

    private int _modificationType;

    private AttributeSet _attributes;

    private String _branch;

    private String _group;

    private String _userMember;

    private String _modifierUser;

    private Date _lastModified;

    private RuleDecisionPoint _decisionPoint;

    private Map<String, Integer> _emailConfirmation;

    private Map<String, String> _emailConfirmationControl;

    private static File _transaction_directory;

    static {
        _transaction_directory = new File(WBSAgnitioConfiguration.getIDMDirectory() + "/engine/rules/confirmations/transaction");
        if (!_transaction_directory.exists()) {
            _transaction_directory.mkdirs();
        }
        VALID_CHARS = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
        HTML_CHARS = new HashMap<Integer, String>();
        HTML_CHARS.put((int) 'á', "&aacute;");
        HTML_CHARS.put((int) 'é', "&eacute;");
        HTML_CHARS.put((int) 'í', "&iacute;");
        HTML_CHARS.put((int) 'ó', "&oacute;");
        HTML_CHARS.put((int) 'ú', "&uacute;");
        HTML_CHARS.put((int) 'Á', "&Aacute;");
        HTML_CHARS.put((int) 'É', "&Eacute;");
        HTML_CHARS.put((int) 'Í', "&Iacute;");
        HTML_CHARS.put((int) 'Ó', "&Oacute;");
        HTML_CHARS.put((int) 'Ú', "&Uacute;");
        HTML_CHARS.put((int) 'ñ', "&ntilde;");
        HTML_CHARS.put((int) 'Ñ', "&Ntilde;");
    }

    protected RuleDecisionPointTransaction(int type, String modifierUser, String group, String user, List<String> roles, RuleDecisionPoint rdp) throws Exception {
        if (rdp == null) {
            throw new Exception("invalid rule decision point");
        }
        if (group == null || group.isEmpty()) {
            throw new Exception("invalid group");
        }
        if (user == null || user.isEmpty()) {
            throw new Exception("invalid user");
        }
        if (type != RuleEngine.GROUP_USER_ADD && type != RuleEngine.GROUP_USER_REMOVE) {
            throw new Exception("invalid operation type for constructor");
        }
        this._uuid = generateUUID();
        while (exists(this._uuid)) {
            this._uuid = generateUUID();
        }
        this._lastModified = new Date();
        this._modificationType = type;
        this._modifierUser = modifierUser;
        this._group = group;
        this._userMember = user;
        this._decisionPoint = rdp;
        this._emailConfirmation = new HashMap<String, Integer>();
        this._emailConfirmationControl = new HashMap<String, String>();
        for (String _mail : this._decisionPoint.getRecipientMail()) {
            this._emailConfirmation.put(_mail, CONFIRMATION_NOT_ANSWERED);
            this._emailConfirmationControl.put(_mail, generateUUID());
        }
        String _message = getMessage(type, roles);
        try {
            for (String _email : this._emailConfirmation.keySet()) {
                sendMail(rdp, _email, _message);
            }
        } catch (Exception _ex) {
            throw new Exception("cannot send confirmation mail - " + _ex.getMessage());
        }
        write();
    }

    protected RuleDecisionPointTransaction(int type, String modifierUser, AttributeSet attributes, String branch, RuleDecisionPoint rdp) throws Exception {
        if (rdp == null) {
            throw new Exception("invalid rule decision point");
        }
        if (attributes == null) {
            throw new Exception("invalid attributes");
        }
        if (type == RuleEngine.GROUP_USER_ADD || type == RuleEngine.GROUP_USER_REMOVE) {
            throw new Exception("invalid operation type for constructor");
        }
        this._uuid = generateUUID();
        while (exists(this._uuid)) {
            this._uuid = generateUUID();
        }
        this._lastModified = new Date();
        this._attributes = attributes;
        this._modificationType = type;
        this._modifierUser = modifierUser;
        this._branch = branch;
        this._decisionPoint = rdp;
        this._emailConfirmation = new HashMap<String, Integer>();
        this._emailConfirmationControl = new HashMap<String, String>();
        for (String _mail : this._decisionPoint.getRecipientMail()) {
            this._emailConfirmation.put(_mail, CONFIRMATION_NOT_ANSWERED);
            this._emailConfirmationControl.put(_mail, generateUUID());
        }
        String _message = getMessage(type, null);
        try {
            for (String _email : this._emailConfirmation.keySet()) {
                sendMail(rdp, _email, _message);
            }
        } catch (Exception _ex) {
            throw new Exception("cannot send confirmation mail - " + _ex.getMessage());
        }
        write();
    }

    public RuleDecisionPointTransaction(String uuid) throws Exception {
        if (!exists(uuid)) {
            throw new Exception("decision point does not exists");
        }
        this._uuid = uuid;
        this._attributes = new AttributeSet();
        this._emailConfirmation = new HashMap<String, Integer>();
        this._emailConfirmationControl = new HashMap<String, String>();
        load();
    }

    public static List<RuleDecisionPointTransaction> RuleDecisionPointsTransactionByRuleDecisionPoint(String nameDecisionPoint) throws Exception {
        List<RuleDecisionPointTransaction> _transactions = new ArrayList<RuleDecisionPointTransaction>();
        File dir = new File(_transaction_directory.getAbsolutePath() + "/");
        String[] ficheros = dir.list();
        if (ficheros != null) {
            for (int x = 0; x < ficheros.length; x++) {
                String _nameFile = ficheros[x];
                DocumentBuilder _db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
                File _f = new File(_transaction_directory.getAbsolutePath() + "/" + _nameFile);
                Document _doc = null;
                try {
                    _doc = _db.parse(_f);
                } catch (Exception ex) {
                }
                NodeList _nl = _doc.getElementsByTagName("decision-point");
                if (_nl.getLength() == 1) {
                    Node _n = _nl.item(0);
                    if (_n.getFirstChild() != null && nameDecisionPoint.equals(_n.getFirstChild().getNodeValue())) {
                        RuleDecisionPointTransaction _aux = new RuleDecisionPointTransaction(_nameFile.substring(0, _nameFile.length() - 4));
                        _aux.getAttributes().setAttribute("lastModified", Long.valueOf(_f.lastModified()));
                        _transactions.add(_aux);
                    }
                }
            }
        }
        return _transactions;
    }

    public void destroy() {
        File _f = new File(_transaction_directory.getAbsolutePath() + "/" + this._uuid + ".xml");
        if (_f.exists()) {
            _f.delete();
        }
    }

    public static boolean exists(String uuid) {
        File _f = new File(_transaction_directory.getAbsolutePath() + "/" + uuid + ".xml");
        return _f.exists();
    }

    private static String generateUUID() {
        try {
            StringBuilder _sb = new StringBuilder();
            Random _r = new Random();
            for (int i = 30; i > 0; --i) {
                _sb.append(VALID_CHARS[_r.nextInt(VALID_CHARS.length)]);
            }
            return _sb.toString();
        } catch (Exception _ex) {
        }
        return null;
    }

    public AttributeSet getAttributes() {
        return this._attributes;
    }

    public String getBranch() throws Exception {
        return this._branch;
    }

    public RuleDecisionPoint getDecisionPoint() throws Exception {
        return this._decisionPoint;
    }

    public String getGroup() throws Exception {
        return this._group;
    }

    private static String getHTMLCharacters(String text) {
        for (Integer _char : HTML_CHARS.keySet()) {
            text = text.replace(new String(new char[] { (char) _char.intValue() }), HTML_CHARS.get(_char));
        }
        return text;
    }

    public String getMemberUser() throws Exception {
        return this._userMember;
    }

    private String getMessage(int type, List<String> roles) throws Exception {
        StringBuilder _sb = new StringBuilder();
        if (this._decisionPoint.getConfirmationType() == RuleDecisionPoint.RULE_CONFIRMATION_MAIL_INFORMATION) {
            _sb.append("User <strong>");
            _sb.append(this._modifierUser);
            _sb.append("</strong> ");
            switch(type) {
                case RuleEngine.USER_ADD:
                    {
                        _sb.append("has added the following user");
                    }
                    break;
                case RuleEngine.USER_UPDATE:
                    {
                        _sb.append("has modified the following user");
                    }
                    break;
                case RuleEngine.USER_REMOVE:
                    {
                        _sb.append("has removed the following user");
                    }
                    break;
                case RuleEngine.GROUP_ADD:
                    {
                        _sb.append("has added the following group");
                    }
                    break;
                case RuleEngine.GROUP_UPDATE:
                    {
                        _sb.append("has modified the following group");
                    }
                    break;
                case RuleEngine.GROUP_REMOVE:
                    {
                        _sb.append("has removed the following group");
                    }
                    break;
                case RuleEngine.GROUP_USER_ADD:
                    {
                        _sb.append("has added the user <strong>");
                        _sb.append(this._userMember);
                        _sb.append("</strong> to the group <strong>");
                        _sb.append(this._group);
                        _sb.append("</strong>");
                        if (roles != null && !roles.isEmpty()) {
                            _sb.append(", as would be assigned the following roles");
                        }
                    }
                    break;
                case RuleEngine.GROUP_USER_REMOVE:
                    {
                        _sb.append("is trying to remove the user <strong>");
                        _sb.append(this._userMember);
                        _sb.append("</strong> from the group <strong>");
                        _sb.append(this._group);
                        _sb.append("</strong>");
                        if (roles != null && !roles.isEmpty()) {
                            _sb.append(", as would be deallocated the following roles");
                        }
                    }
                    break;
                default:
                    {
                        throw new Exception("invalid operation type");
                    }
            }
            _sb.append(":<br/><br/>\n");
            if ((type == RuleEngine.GROUP_USER_ADD || type == RuleEngine.GROUP_USER_REMOVE) && roles != null) {
                for (String _role : roles) {
                    _sb.append("<strong>");
                    _sb.append(_role);
                    _sb.append("</strong><br/>\n");
                }
            } else if (this._attributes != null) {
                for (String _name : this._attributes.getAttributeNames()) {
                    if (_name.equalsIgnoreCase("password")) {
                        continue;
                    }
                    if (this._attributes.getAttribute(_name) != null) {
                        for (Object _o : this._attributes.getAttribute(_name)) {
                            _sb.append("<strong>");
                            _sb.append(_name);
                            _sb.append("</strong>: ");
                            _sb.append(String.valueOf(_o));
                            _sb.append("<br/>\n");
                        }
                    }
                }
            }
            _sb.append("<br/>\n");
        } else if (this._decisionPoint.getConfirmationType() == RuleDecisionPoint.RULE_CONFIRMATION_MAIL_FROM_ANY || this._decisionPoint.getConfirmationType() == RuleDecisionPoint.RULE_CONFIRMATION_MAIL_FROM_ALL) {
            _sb.append("User <strong>");
            _sb.append(this._modifierUser);
            _sb.append("</strong> ");
            switch(type) {
                case RuleEngine.USER_ADD:
                    {
                        _sb.append("is trying to add the following user");
                    }
                    break;
                case RuleEngine.USER_UPDATE:
                    {
                        _sb.append("is trying to modify the following user");
                    }
                    break;
                case RuleEngine.USER_REMOVE:
                    {
                        _sb.append("is trying to remove the following user");
                    }
                    break;
                case RuleEngine.GROUP_ADD:
                    {
                        _sb.append("is trying to add the following group");
                    }
                    break;
                case RuleEngine.GROUP_UPDATE:
                    {
                        _sb.append("is trying to modify the following group");
                    }
                    break;
                case RuleEngine.GROUP_REMOVE:
                    {
                        _sb.append("is trying to remove the following group");
                    }
                    break;
                case RuleEngine.GROUP_USER_ADD:
                    {
                        _sb.append("is trying to add the user <strong>");
                        _sb.append(this._userMember);
                        _sb.append("</strong> to the group <strong>");
                        _sb.append(this._group);
                        _sb.append("</strong>");
                        if (roles != null && !roles.isEmpty()) {
                            _sb.append(", as would be assigned the following roles");
                        }
                    }
                    break;
                case RuleEngine.GROUP_USER_REMOVE:
                    {
                        _sb.append("is trying to remove the user <strong>");
                        _sb.append(this._userMember);
                        _sb.append("</strong> from the group <strong>");
                        _sb.append(this._group);
                        _sb.append("</strong>");
                        if (roles != null && !roles.isEmpty()) {
                            _sb.append(", as would be deallocated the following roles");
                        }
                    }
                    break;
                default:
                    {
                        throw new Exception("invalid operation type");
                    }
            }
            _sb.append(":<br/><br/>\n");
            if ((type == RuleEngine.GROUP_USER_ADD || type == RuleEngine.GROUP_USER_REMOVE) && roles != null) {
                for (String _role : roles) {
                    _sb.append("<strong>");
                    _sb.append(_role);
                    _sb.append("</strong><br/>\n");
                }
            } else if (this._attributes != null) {
                for (String _name : this._attributes.getAttributeNames()) {
                    if (_name.equalsIgnoreCase("password")) {
                        continue;
                    }
                    if (this._attributes.getAttribute(_name) != null) {
                        for (Object _o : this._attributes.getAttribute(_name)) {
                            _sb.append("<strong>");
                            _sb.append(_name);
                            _sb.append("</strong>: ");
                            _sb.append(String.valueOf(_o));
                            _sb.append("<br/>\n");
                        }
                    }
                }
            }
            _sb.append("<br/>\n");
        } else if (this._decisionPoint.getConfirmationType() == RuleDecisionPoint.RULE_CONFIRMATION_MAIL_ATTRIBUTES) {
            _sb.append("User <strong>");
            _sb.append(this._modifierUser);
            _sb.append("</strong> ");
            switch(type) {
                case RuleEngine.USER_ADD:
                    {
                        _sb.append("is trying to add the user <strong>");
                        _sb.append(this._attributes.getAttributeFirstStringValue("uid"));
                        _sb.append("</strong>. Please fill some attributes for this user");
                    }
                    break;
                case RuleEngine.USER_UPDATE:
                    {
                        _sb.append("is trying to modify the user <strong>");
                        _sb.append(this._attributes.getAttributeFirstStringValue("uid"));
                        _sb.append("</strong>. Please fill some attributes for this user");
                    }
                    break;
                case RuleEngine.GROUP_ADD:
                    {
                        _sb.append("is trying to add group <strong>");
                        _sb.append(this._attributes.getAttributeFirstStringValue("cn"));
                        _sb.append("</strong>. Please fill some attributes for this user");
                    }
                    break;
                case RuleEngine.GROUP_UPDATE:
                    {
                        _sb.append("is trying to modify group <strong>");
                        _sb.append(this._attributes.getAttributeFirstStringValue("cn"));
                        _sb.append("</strong>. Please fill some attributes for this user");
                    }
                    break;
                default:
                    {
                        throw new Exception("invalid operation type");
                    }
            }
            _sb.append(":<br/><br/>\n");
        }
        return getHTMLCharacters(_sb.toString());
    }

    public int getModificationType() {
        return this._modificationType;
    }

    public String getModifierUser() {
        return this._modifierUser;
    }

    public String getUUID() {
        return this._uuid;
    }

    public boolean isBlocking() {
        if (this._decisionPoint.getConfirmationType() == RuleDecisionPoint.RULE_CONFIRMATION_MAIL_INFORMATION) {
            return false;
        }
        return true;
    }

    private void load() throws Exception {
        File _f = new File(_transaction_directory.getAbsolutePath() + "/" + this._uuid + ".xml");
        if (_f.exists()) {
            DocumentBuilder _db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document _doc = _db.parse(_f);
            NodeList _nl = _doc.getFirstChild().getChildNodes();
            for (int i = 0; i < _nl.getLength(); i++) {
                if (_nl.item(i).getNodeType() == Node.ELEMENT_NODE) {
                    if ("modifier".equals(_nl.item(i).getNodeName())) {
                        this._modifierUser = _nl.item(i).getTextContent();
                    } else if ("modification".equals(_nl.item(i).getNodeName())) {
                        try {
                            this._modificationType = Integer.parseInt(_nl.item(i).getTextContent());
                        } catch (Exception _ex) {
                            throw new Exception("invalid modification type");
                        }
                    } else if ("branch".equals(_nl.item(i).getNodeName())) {
                        this._branch = _nl.item(i).getTextContent();
                    } else if ("group".equals(_nl.item(i).getNodeName())) {
                        this._group = _nl.item(i).getTextContent();
                    } else if ("lastModified".equals(_nl.item(i).getNodeName())) {
                        SimpleDateFormat formato = new SimpleDateFormat("yyyy.MM.dd G '--' hh:mm:ss z");
                        this._lastModified = formato.parse(_nl.item(i).getTextContent());
                    } else if ("user".equals(_nl.item(i).getNodeName())) {
                        this._userMember = _nl.item(i).getTextContent();
                    } else if ("decision-point".equals(_nl.item(i).getNodeName())) {
                        this._decisionPoint = new RuleDecisionPoint(_nl.item(i).getTextContent());
                    } else if ("confirmations".equals(_nl.item(i).getNodeName())) {
                        Element _e_confirmations = (Element) _nl.item(i);
                        NodeList _nl_childs = _e_confirmations.getChildNodes();
                        for (int j = 0; j < _nl_childs.getLength(); j++) {
                            if (_nl_childs.item(j).getNodeType() == Node.ELEMENT_NODE && "confirmation".equals(_nl_childs.item(j).getNodeName())) {
                                Element _e_confirmation = (Element) _nl_childs.item(j);
                                if (_e_confirmation.hasAttribute("email") && _e_confirmation.hasAttribute("control")) {
                                    int _status = -1;
                                    try {
                                        _status = Integer.parseInt(_e_confirmation.getTextContent());
                                    } catch (Exception _ex) {
                                        throw new Exception("invalid confirmation status");
                                    }
                                    if (_status != CONFIRMATION_NOT_ANSWERED && _status != CONFIRMATION_CONFIRMED && _status != CONFIRMATION_REJECTED) {
                                        throw new Exception("invalid confirmation status");
                                    }
                                    this._emailConfirmation.put(_e_confirmation.getAttribute("email"), _status);
                                    this._emailConfirmationControl.put(_e_confirmation.getAttribute("email"), _e_confirmation.getAttribute("control"));
                                }
                            }
                        }
                    } else if ("attributes".equals(_nl.item(i).getNodeName())) {
                        Element _e_entry = (Element) _nl.item(i);
                        NodeList _nl_childs = _e_entry.getChildNodes();
                        for (int j = 0; j < _nl_childs.getLength(); j++) {
                            if (_nl_childs.item(j).getNodeType() == Node.ELEMENT_NODE && "attribute".equals(_nl_childs.item(j).getNodeName())) {
                                Element _e_attribute = (Element) _nl_childs.item(j);
                                if (this._attributes.hasAttribute(_e_attribute.getAttribute("name"))) {
                                    List<Object> _values = new ArrayList<Object>();
                                    _values.addAll(Arrays.asList(_attributes.getAttribute(_e_attribute.getAttribute("name"))));
                                    _values.add(_e_attribute.getTextContent());
                                    this._attributes.setAttribute(_e_attribute.getAttribute("name"), _values.toArray());
                                } else {
                                    this._attributes.setAttribute(_e_attribute.getAttribute("name"), new Object[] { _nl_childs.item(j).getTextContent() });
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private void sendMail(RuleDecisionPoint rdp, String mail, String text) throws Exception {
        String _title;
        CustomTransport _mt = new CustomTransport();
        Map<String, String> _fields = new HashMap<String, String>();
        if (rdp.getConfirmationType() == RuleDecisionPoint.RULE_CONFIRMATION_MAIL_INFORMATION) {
            _title = "WBSAGNITIO-INFORMATION";
        } else {
            _title = "WBSAGNITIO-CONFIRMATION";
        }
        _fields.put("Message", text);
        if (rdp.getConfirmationType() == RuleDecisionPoint.RULE_CONFIRMATION_MAIL_FROM_ANY || rdp.getConfirmationType() == RuleDecisionPoint.RULE_CONFIRMATION_MAIL_FROM_ALL) {
            Map<String, URL> _buttons = new HashMap<String, URL>();
            StringBuilder _url_base = new StringBuilder();
            _url_base.append("http");
            if (TomcatConfiguration.checkHTTPS()) {
                _url_base.append("s");
            }
            _url_base.append("://");
            _url_base.append(NetworkManager.getLocalAddress());
            _url_base.append("/admin/Comm");
            StringBuilder _sb = new StringBuilder();
            _sb.append(_url_base.toString());
            _sb.append("?type=");
            _sb.append(CommResponse.TYPE_RULE_CONFIRMATION);
            _sb.append("&command=");
            _sb.append(CommResponse.COMMAND_EMAIL_ACCEPT);
            _sb.append("&uuid=");
            _sb.append(this._uuid);
            _sb.append("&email=");
            _sb.append(mail);
            _sb.append("&control=");
            _sb.append(this._emailConfirmationControl.get(mail));
            _buttons.put("Accept", new URL(_sb.toString()));
            _sb = new StringBuilder();
            _sb.append(_url_base.toString());
            _sb.append("?type=");
            _sb.append(CommResponse.TYPE_RULE_CONFIRMATION);
            _sb.append("&command=");
            _sb.append(CommResponse.COMMAND_EMAIL_REJECT);
            _sb.append("&uuid=");
            _sb.append(this._uuid);
            _sb.append("&email=");
            _sb.append(mail);
            _sb.append("&control=");
            _sb.append(this._emailConfirmationControl.get(mail));
            _buttons.put("Reject", new URL(_sb.toString()));
            _mt.setMailButtons(_buttons);
        } else if (rdp.getConfirmationType() == RuleDecisionPoint.RULE_CONFIRMATION_MAIL_ATTRIBUTES) {
            Map<String, URL> _buttons = new HashMap<String, URL>();
            StringBuilder _url_base = new StringBuilder();
            _url_base.append("http");
            if (TomcatConfiguration.checkHTTPS()) {
                _url_base.append("s");
            }
            _url_base.append("://");
            _url_base.append(NetworkManager.getLocalAddress());
            _url_base.append("/admin/Comm");
            StringBuilder _sb = new StringBuilder();
            _sb.append(_url_base.toString());
            _sb.append("?type=");
            _sb.append(CommResponse.TYPE_RULE_CONFIRMATION);
            _sb.append("&command=");
            _sb.append(CommResponse.COMMAND_EMAIL_ATTRIBUTES);
            _sb.append("&uuid=");
            _sb.append(this._uuid);
            _sb.append("&email=");
            _sb.append(mail);
            _sb.append("&control=");
            _sb.append(this._emailConfirmationControl.get(mail));
            _buttons.put("Proceed", new URL(_sb.toString()));
            _mt.setMailButtons(_buttons);
        }
        _mt.sendMail(mail, _title);
    }

    public int setConfirmation(String mail, String control, int status) throws Exception {
        if (mail == null) {
            throw new Exception("invalid email");
        }
        if (!this._emailConfirmation.containsKey(mail)) {
            throw new Exception("this email should not confirm this transaction");
        }
        if (control == null || !this._emailConfirmationControl.containsKey(mail) || !control.equals(this._emailConfirmationControl.get(mail))) {
            throw new Exception("invalid email control secuence");
        }
        switch(status) {
            case CONFIRMATION_CONFIRMED:
                {
                    switch(this._decisionPoint.getConfirmationType()) {
                        case RuleDecisionPoint.RULE_CONFIRMATION_MAIL_FROM_ALL:
                            {
                                boolean _all_confirmed = true;
                                this._emailConfirmation.put(mail, status);
                                for (int _status : this._emailConfirmation.values()) {
                                    if (_status != CONFIRMATION_CONFIRMED) {
                                        _all_confirmed = false;
                                    }
                                }
                                if (_all_confirmed) {
                                    return TRANSACTION_CONFIRMED;
                                }
                                write();
                            }
                            break;
                        case RuleDecisionPoint.RULE_CONFIRMATION_MAIL_FROM_ANY:
                            {
                                return TRANSACTION_CONFIRMED;
                            }
                        default:
                            throw new Exception("transaction type cannot be confirmed or rejected");
                    }
                }
                break;
            case CONFIRMATION_REJECTED:
                {
                    switch(this._decisionPoint.getConfirmationType()) {
                        case RuleDecisionPoint.RULE_CONFIRMATION_MAIL_FROM_ALL:
                            {
                                return TRANSACTION_REJECTED;
                            }
                        case RuleDecisionPoint.RULE_CONFIRMATION_MAIL_FROM_ANY:
                            {
                                boolean _all_rejected = true;
                                this._emailConfirmation.put(mail, status);
                                for (int _status : this._emailConfirmation.values()) {
                                    if (_status != CONFIRMATION_REJECTED) {
                                        _all_rejected = false;
                                    }
                                }
                                if (_all_rejected) {
                                    return TRANSACTION_REJECTED;
                                }
                                write();
                            }
                            break;
                        default:
                            throw new Exception("transaction type cannot be confirmed or rejected");
                    }
                }
                break;
            default:
                throw new Exception("invalid confirmation status");
        }
        return TRANSACTION_IN_PROCESS;
    }

    public void setConfirmation(String email, String control, AttributeSet attributes) throws Exception {
        if (!this._emailConfirmation.containsKey(email)) {
            throw new Exception("this email should not confirm this transaction");
        }
        if (this._decisionPoint.getConfirmationType() != RuleDecisionPoint.RULE_CONFIRMATION_MAIL_ATTRIBUTES) {
            throw new Exception("you cannot confirm attributes for this transaction type");
        }
        if (control == null || !this._emailConfirmationControl.containsKey(email) || !control.equals(this._emailConfirmationControl.get(email))) {
            throw new Exception("invalid email control secuence");
        }
    }

    private void write() throws Exception {
        if (this._uuid == null) {
            throw new Exception("invalid transaction decision point uuid");
        }
        if (!isBlocking()) {
            return;
        }
        SimpleDateFormat formato = new SimpleDateFormat("yyyy.MM.dd G '--' hh:mm:ss z");
        DocumentBuilder _db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        Document _doc = _db.newDocument();
        Element _e_transaction = _doc.createElement("transaction");
        Element _e_modifier = _doc.createElement("modifier");
        Element _e_modification = _doc.createElement("modification");
        Element _e_lastModified = _doc.createElement("lastModified");
        Element _e_decision_point = _doc.createElement("decision-point");
        _e_modifier.setTextContent(String.valueOf(this._modifierUser));
        _e_modification.setTextContent(String.valueOf(this._modificationType));
        _e_lastModified.setTextContent(formato.format(this._lastModified));
        _e_decision_point.setTextContent(this._decisionPoint.getName());
        _e_transaction.appendChild(_e_modifier);
        _e_transaction.appendChild(_e_modification);
        _e_transaction.appendChild(_e_decision_point);
        _e_transaction.appendChild(_e_lastModified);
        if (this._branch != null && !this._branch.isEmpty()) {
            Element _e_branch = _doc.createElement("branch");
            _e_branch.setTextContent(this._branch);
            _e_transaction.appendChild(_e_branch);
        }
        if (this._group != null && this._userMember != null) {
            Element _e_group = _doc.createElement("group");
            Element _e_user = _doc.createElement("user");
            _e_group.setTextContent(this._group);
            _e_user.setTextContent(this._userMember);
            _e_transaction.appendChild(_e_group);
            _e_transaction.appendChild(_e_user);
        }
        Element _e_confirmations = _doc.createElement("confirmations");
        for (String _mail : this._emailConfirmation.keySet()) {
            Element _e_confirmation = _doc.createElement("confirmation");
            _e_confirmation.setAttribute("email", _mail);
            _e_confirmation.setAttribute("control", this._emailConfirmationControl.get(_mail));
            _e_confirmation.setTextContent(String.valueOf(this._emailConfirmation.get(_mail)));
            _e_confirmations.appendChild(_e_confirmation);
        }
        _e_transaction.appendChild(_e_confirmations);
        if (this._attributes != null) {
            Element _e_entry = _doc.createElement("attributes");
            for (String _name : this._attributes.getAttributeNames()) {
                if (this._attributes.hasAttribute(_name)) {
                    for (Object _o : this._attributes.getAttribute(_name)) {
                        if (_o instanceof String) {
                            Element _e_attribute = _doc.createElement("attribute");
                            _e_attribute.setAttribute("name", _name);
                            _e_attribute.setTextContent(String.valueOf(_o));
                            _e_entry.appendChild(_e_attribute);
                        }
                    }
                }
            }
            _e_transaction.appendChild(_e_entry);
        }
        File _f = new File(_transaction_directory.getAbsolutePath() + "/" + this._uuid + ".xml");
        _doc.appendChild(_e_transaction);
        Source source = new DOMSource(_doc);
        Result result = new StreamResult(_f);
        FileLock _fl = new FileLock(_f);
        Transformer _t = TransformerFactory.newInstance().newTransformer();
        try {
            _fl.lock();
            _t.transform(source, result);
        } finally {
            _fl.unlock();
        }
    }

    public Map<String, Integer> get_emailConfirmation() {
        return _emailConfirmation;
    }

    public void set_emailConfirmation(Map<String, Integer> emailConfirmation) {
        _emailConfirmation = emailConfirmation;
    }

    public Date get_lastModified() {
        return _lastModified;
    }

    public void set_lastModified(Date lastModified) {
        _lastModified = lastModified;
    }
}
