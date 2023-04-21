package ch.bbv.pim.formats;

import java.io.*;
import org.apache.commons.codec.binary.Base64;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import ch.bbv.pim.AddressBook;
import ch.bbv.pim.Contact;
import ch.bbv.pim.Organization;

public class LdifEmitter extends ContactEmitter {

    /**
   * The velocity template used to generate the representation of contacts.
   */
    private Template template;

    /**
   * Default constructor of the class.
   */
    public LdifEmitter() {
        template = loadTemplate("ldif.vm");
    }

    @Override
    public void emit(AddressBook book, File records) throws IOException {
        assert (book != null) && ((records != null) && (!records.exists() || records.canWrite()));
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(records)));
        VelocityContext context = new VelocityContext();
        context.put("emitter", this);
        context.put("addressBook", book);
        for (Organization organization : book.getOrganizations()) {
            context.put("organization", organization);
            for (Contact contact : organization.getContacts()) {
                context.put("contact", contact);
                String vcard = processTemplate(template, context);
                writer.write(vcard);
            }
        }
        writer.close();
    }

    /**
   * Returns true if the text should be encoded.
   * @param text text to check
   * @return true if the text should be encoded, false otherwise.
   * @throws UnsupportedEncodingException if the UTF-8 transformation encounters
   *           a problem.
   */
    public boolean shouldTransform(String text) throws UnsupportedEncodingException {
        byte[] bytes = text.getBytes("UTF-8");
        return bytes.length > text.length();
    }

    /**
   * Encodes the text into UTF-8 base 64 representation.
   * @param text text to encode.
   * @return the encoded text.
   * @throws UnsupportedEncodingException if the UTF-8 transformation encounters
   *           a problem.
   */
    public String transform(String text) throws UnsupportedEncodingException {
        return new String(Base64.encodeBase64(text.getBytes("UTF-8")));
    }
}
