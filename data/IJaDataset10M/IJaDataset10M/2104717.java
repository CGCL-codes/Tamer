package nl.knaw.dans.common.dataperfect;

/**
 * Internal interface that represents the database-wide settings.
 *
 * @author Jan van Mansum
 */
interface DatabaseSettings {

    String getDefaultDateOrder();

    String getDefaultTimeOrder();

    String getCharsetName();

    String getExtAsciiCodeStartDelimiter();

    String getExtAsciiCodeEndDelimiter();
}
