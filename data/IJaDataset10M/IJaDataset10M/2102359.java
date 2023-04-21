package net.sf.elbe.ui.actions;

import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;
import net.sf.elbe.core.ELBECoreConstants;
import net.sf.elbe.core.model.DN;
import net.sf.elbe.core.model.IAttribute;
import net.sf.elbe.core.model.IValue;
import net.sf.elbe.core.utils.LdifUtils;
import net.sf.elbe.core.utils.ModelConverter;
import net.sf.elbe.ui.ELBEUIPlugin;
import net.sf.elbe.ui.ELBEUIConstants;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;

public class CopyValueAction extends ElbeAction {

    public static final int MODE_UTF8 = 1;

    public static final int MODE_BASE64 = 2;

    public static final int MODE_HEX = 3;

    public static final int MODE_LDIF = 4;

    private int mode;

    public CopyValueAction(int mode) {
        this.mode = mode;
    }

    public String getText() {
        if (mode == MODE_UTF8) {
            return getValueSet().size() > 1 ? "Copy Values (UTF-8)" : "Copy Value (UTF-8)";
        } else if (mode == MODE_BASE64) {
            return getValueSet().size() > 1 ? "Copy Values (BASE-64)" : "Copy Value (BASE-64)";
        } else if (mode == MODE_HEX) {
            return getValueSet().size() > 1 ? "Copy Values (HEX)" : "Copy Value (HEX)";
        } else if (mode == MODE_LDIF) {
            return getValueSet().size() > 1 ? "Copy Name-Value-Pairs as LDIF" : "Copy Name-Value-Pair as LDIF";
        } else {
            return "Copy Value";
        }
    }

    public ImageDescriptor getImageDescriptor() {
        if (mode == MODE_UTF8) {
            return ELBEUIPlugin.getDefault().getImageDescriptor(ELBEUIConstants.IMG_COPY_UTF8);
        } else if (mode == MODE_BASE64) {
            return ELBEUIPlugin.getDefault().getImageDescriptor(ELBEUIConstants.IMG_COPY_BASE64);
        } else if (mode == MODE_HEX) {
            return ELBEUIPlugin.getDefault().getImageDescriptor(ELBEUIConstants.IMG_COPY_HEX);
        } else if (mode == MODE_LDIF) {
            return ELBEUIPlugin.getDefault().getImageDescriptor(ELBEUIConstants.IMG_COPY_LDIF);
        } else {
            return null;
        }
    }

    public String getCommandId() {
        return null;
    }

    public boolean isEnabled() {
        return getValueSet().size() > 0 || getSelectedSearchResults().length > 0;
    }

    public void run() {
        StringBuffer text = new StringBuffer();
        Set valueSet = getValueSet();
        if (!valueSet.isEmpty()) {
            for (Iterator iterator = valueSet.iterator(); iterator.hasNext(); ) {
                IValue value = (IValue) iterator.next();
                if (mode == MODE_UTF8) {
                    text.append(LdifUtils.utf8decode(value.getBinaryValue()));
                    if (iterator.hasNext()) text.append(ELBECoreConstants.LINE_SEPARATOR);
                } else if (mode == MODE_BASE64) {
                    text.append(LdifUtils.base64encode(value.getBinaryValue()));
                    if (iterator.hasNext()) text.append(ELBECoreConstants.LINE_SEPARATOR);
                } else if (mode == MODE_HEX) {
                    text.append(LdifUtils.hexEncode(value.getBinaryValue()));
                    if (iterator.hasNext()) text.append(ELBECoreConstants.LINE_SEPARATOR);
                } else if (mode == MODE_LDIF) {
                    text.append(ModelConverter.valueToLdifAttrValLine(value).toFormattedString());
                }
            }
        } else if (getSelectedSearchResults().length > 0) {
            DN dn = getSelectedSearchResults()[0].getDn();
            if (mode == MODE_UTF8) {
                text.append(LdifUtils.utf8decode(dn.toString().getBytes()));
            } else if (mode == MODE_BASE64) {
                text.append(LdifUtils.base64encode(dn.toString().getBytes()));
            } else if (mode == MODE_HEX) {
                text.append(LdifUtils.hexEncode(dn.toString().getBytes()));
            } else if (mode == MODE_LDIF) {
                text.append(ModelConverter.dnToLdifDnLine(dn).toFormattedString());
            }
        }
        if (text.length() > 0) {
            CopyAction.copyToClipboard(new Object[] { text.toString() }, new Transfer[] { TextTransfer.getInstance() });
        }
    }

    protected Set getValueSet() {
        Set valueSet = new LinkedHashSet();
        for (int i = 0; i < getSelectedAttributeHierarchies().length; i++) {
            for (Iterator it = getSelectedAttributeHierarchies()[i].iterator(); it.hasNext(); ) {
                IAttribute att = (IAttribute) it.next();
                valueSet.addAll(Arrays.asList(att.getValues()));
            }
        }
        for (int i = 0; i < getSelectedAttributes().length; i++) {
            valueSet.addAll(Arrays.asList(getSelectedAttributes()[i].getValues()));
        }
        valueSet.addAll(Arrays.asList(getSelectedValues()));
        return valueSet;
    }
}
