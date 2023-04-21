package gleam.docservice.proxy.iaa;

/**
 * Interface representing a single F-measure statistic.
 */
public interface FMeasure {

    public float correct();

    public float partiallyCorrect();

    public float spurious();

    public float missing();

    public float precision();

    public float recall();

    public float f1();

    public float precisionLenient();

    public float recallLenient();

    public float f1Lenient();
}
