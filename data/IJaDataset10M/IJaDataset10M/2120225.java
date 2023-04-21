package au.gov.naa.digipres.xena.plugin.audio;

import au.gov.naa.digipres.xena.kernel.XenaException;
import au.gov.naa.digipres.xena.kernel.guesser.DefaultGuesser;
import au.gov.naa.digipres.xena.kernel.guesser.FileTypeDescriptor;
import au.gov.naa.digipres.xena.kernel.guesser.GuesserManager;
import au.gov.naa.digipres.xena.kernel.type.Type;

public class AiffGuesser extends DefaultGuesser {

    private static final byte[][] aiffMagic = { { 0x46, 0x4F, 0x52, 0x4D, 0x00 } };

    private static final String[] aiffExtensions = { "aif", "aiff" };

    private static final String[] aiffMime = { "audio/aiff" };

    private FileTypeDescriptor[] descriptorArr;

    private Type type;

    /**
	 * @throws XenaException 
	 * 
	 */
    public AiffGuesser() {
        super();
    }

    @Override
    public void initGuesser(GuesserManager guesserManagerParam) throws XenaException {
        guesserManager = guesserManagerParam;
        type = getTypeManager().lookup(AiffType.class);
        FileTypeDescriptor[] tempFileDescriptors = { new FileTypeDescriptor(aiffExtensions, aiffMagic, aiffMime, type) };
        descriptorArr = tempFileDescriptors;
    }

    @Override
    public String getName() {
        return "AIFFGuesser";
    }

    @Override
    public Type getType() {
        return type;
    }

    @Override
    protected FileTypeDescriptor[] getFileTypeDescriptors() {
        return descriptorArr;
    }
}
