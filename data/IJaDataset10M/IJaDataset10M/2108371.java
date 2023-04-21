package medimagesuite.util.image;

/**
 * @author Marcelo Costa Oliveira
 * @email oliveiramc@gmail.com
 */
public class DCMImageFile extends AbstractDicomFile implements DCMParams {

    public String getImagePath() {
        return null;
    }

    public short[] getImageData() {
        return null;
    }

    public int getRows() {
        return 0;
    }

    public int getColumns() {
        return 0;
    }

    public int getValueBitsAllocated() {
        return 0;
    }
}
