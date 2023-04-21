package org.fpse.server.impl.formatters;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import org.fpse.server.TaggedOutputStream;
import org.fpse.server.message.FormatingFailedException;

public class AddressFormatter extends AbstractFormatter {

    public AddressFormatter() {
        super();
    }

    public void format(String name, List<String> values, Appendable dest) throws FormatingFailedException, IOException {
        if (null == values || values.isEmpty()) {
            dest.append(TaggedOutputStream.NIL_STRING);
            return;
        }
        List<InternetAddress> list = new ArrayList<InternetAddress>(values.size());
        for (String text : values) {
            try {
                list.addAll(Arrays.asList(InternetAddress.parse(text)));
            } catch (AddressException e) {
                throw new FormatingFailedException("Bad Address:" + text, e);
            }
        }
        dest.append('(');
        boolean first = true;
        for (InternetAddress address : list) {
            if (first) first = false; else dest.append(TaggedOutputStream.SP_STRING);
            dest.append('(');
            addStringValue(address.getPersonal(), dest);
            dest.append(TaggedOutputStream.SP_STRING);
            addStringValue(null, dest);
            dest.append(TaggedOutputStream.SP_STRING);
            String x = address.getAddress();
            int idx = x.indexOf('@');
            addStringValue(x.substring(0, idx), dest);
            dest.append(TaggedOutputStream.SP_STRING);
            addStringValue(x.substring(idx + 1), dest);
            dest.append(')');
        }
        dest.append(')');
    }
}
