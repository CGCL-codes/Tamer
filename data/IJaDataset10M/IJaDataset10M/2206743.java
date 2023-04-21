package org.nakedobjects.plugins.xml.profilestore;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;
import java.util.Iterator;
import org.apache.commons.lang.StringUtils;
import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.encoding.DataOutputStreamExtended;
import org.nakedobjects.plugins.xml.objectstore.internal.data.xml.ContentWriter;
import org.nakedobjects.runtime.context.NakedObjectsContext;
import org.nakedobjects.runtime.persistence.PersistenceSession;
import org.nakedobjects.runtime.persistence.services.ServiceUtil;
import org.nakedobjects.runtime.userprofile.Options;
import org.nakedobjects.runtime.userprofile.PerspectiveEntry;
import org.nakedobjects.runtime.userprofile.UserProfile;

public class UserProfileContentWriter implements ContentWriter {

    private final UserProfile userProfile;

    public UserProfileContentWriter(UserProfile userProfile) {
        this.userProfile = userProfile;
    }

    public void write(Writer writer) throws IOException {
        final StringBuffer xml = new StringBuffer();
        xml.append("<profile>\n");
        Options options = userProfile.getOptions();
        writeOptions(xml, options, null, 0);
        xml.append("  <perspectives>\n");
        for (String perspectiveName : userProfile.list()) {
            PerspectiveEntry perspective = userProfile.getPerspective(perspectiveName);
            xml.append("    <perspective" + attribute("name", perspectiveName) + ">\n");
            xml.append("      <services>\n");
            for (Object service : perspective.getServices()) {
                xml.append("        <service " + attribute("id", ServiceUtil.id(service)) + "/>\n");
            }
            xml.append("      </services>\n");
            xml.append("      <objects>\n");
            for (Object object : perspective.getObjects()) {
                NakedObject nakedObject = getPersistenceSession().getAdapterManager().adapterFor(object);
                OutputStream out = new ByteArrayOutputStream();
                DataOutputStreamExtended outputImpl = new DataOutputStreamExtended(out);
                nakedObject.getOid().encode(outputImpl);
                xml.append("        <object>" + "not yet encoding properly" + "</object>\n");
            }
            xml.append("      </objects>\n");
            xml.append("    </perspective>\n");
        }
        xml.append("  </perspectives>\n");
        xml.append("</profile>\n");
        writer.write(xml.toString());
    }

    private void writeOptions(final StringBuffer xml, Options options, String name1, int level) {
        String spaces = StringUtils.repeat("  ", level);
        Iterator<String> names = options.names();
        if (level == 0 || names.hasNext()) {
            xml.append(spaces + "  <options");
            if (name1 != null) {
                xml.append(" id=\"" + name1 + "\"");
            }
            xml.append(">\n");
            while (names.hasNext()) {
                String name = names.next();
                if (options.isOptions(name)) {
                    writeOptions(xml, options.getOptions(name), name, level + 1);
                } else {
                    xml.append(spaces + "    <option" + attribute("id", name) + ">" + options.getString(name) + "</option>\n");
                }
            }
            xml.append(spaces + "  </options>\n");
        }
    }

    private String attribute(final String name, final String value) {
        return " " + name + "=\"" + value + "\"";
    }

    protected static PersistenceSession getPersistenceSession() {
        return NakedObjectsContext.getPersistenceSession();
    }
}
