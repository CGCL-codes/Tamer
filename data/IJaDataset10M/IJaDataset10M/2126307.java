package gov.nasa.jpf.doc;

/**
 * Implementation of a factory for an options database
 * 
 * @author pacowu
 *
 */
class OptionsFactoryImpl implements OptionsFactory {

    public Options getOptions() {
        return new Options();
    }
}
