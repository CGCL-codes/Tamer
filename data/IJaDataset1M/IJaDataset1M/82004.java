package visad.data.bio;

/**
 * LeicaForm is the VisAD data adapter for Leica files.
 *
 * @deprecated Use LociForm with loci.formats.in.LeicaReader
 */
public class LeicaForm extends LociForm {

    public LeicaForm() {
        super(new loci.formats.in.LeicaReader());
    }

    public static void main(String[] args) throws Exception {
        new LeicaForm().testRead(args);
    }
}
