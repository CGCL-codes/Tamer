package nu.staldal.lagoon.core;

/** 
 * A Procucer that consumes an XML stream.
 */
public abstract class XMLStreamConsumer extends Producer {

    private XMLStreamProducer nextProducer;

    void doDestroy() throws java.io.IOException {
        ProducerInterface prod = getNext();
        if (prod instanceof Producer) {
            ((Producer) prod).doDestroy();
        }
        destroy();
    }

    void doBeforeBuild() throws java.io.IOException {
        ProducerInterface prod = getNext();
        if (prod instanceof Producer) {
            ((Producer) prod).doBeforeBuild();
        }
        beforeBuild();
    }

    void doAfterBuild() throws java.io.IOException {
        ProducerInterface prod = getNext();
        if (prod instanceof Producer) {
            ((Producer) prod).doAfterBuild();
        }
        afterBuild();
    }

    /** 
     * Set the next producer. 
     * Used during initialization.
     * 
     * @param next  the next Producer
     */
    void setNext(XMLStreamProducer next) {
        nextProducer = next;
    }

    /** 
     * @return  the next upstream producer
     */
    public XMLStreamProducer getNext() {
        return nextProducer;
    }
}
