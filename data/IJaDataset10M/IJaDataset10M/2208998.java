package messages;

import business_layer.DataDictionary;

/**
 *
 * @author Misia
 */
public class UpdateDictionaryMessage extends BasicMessage {

    private DataDictionary dictionary;

    public UpdateDictionaryMessage(DataDictionary updatedDictionary, int senderId, long timestamp, int messageNumber) {
        super(senderId, timestamp, messageNumber);
        this.dictionary = updatedDictionary;
    }

    @Override
    public String toString() {
        return super.toString() + "with dictionary update";
    }

    /**
     * @return the dictionary
     */
    public DataDictionary getDictionary() {
        return dictionary;
    }

    /**
     * @param dictionary the dictionary to set
     */
    public void setDictionary(DataDictionary dictionary) {
        this.dictionary = dictionary;
    }
}
