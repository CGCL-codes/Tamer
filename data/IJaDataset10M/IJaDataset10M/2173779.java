package net.sf.vietpad.converter;

public class UnicodeConversion {

    Converter converter;

    /**
     *  Constructor for the UnicodeConversion object
     *
     *@param  sourceEncoding  One of supported encodings: "VISCII", "VPS", "VNI", "VIQR/Vietnet",
     *                        "TCVN3 (ABC)", "Unicode", "Unicode Composite", "UTF-8", ISC", or "NCR"
     */
    public UnicodeConversion(final String sourceEncoding) {
        this(VietEncodings.valueOf(sourceEncoding));
    }

    /**
     *  Constructor for the UnicodeConversion object
     *
     *@param  sourceEncoding One of VietEncodings enums
     */
    public UnicodeConversion(final VietEncodings sourceEncoding) {
        converter = ConverterFactory.createConverter(sourceEncoding);
    }

    /**
     *  Converts a string
     *
     *@param  source    Text to be converted
     *@param  html      True if HTML document
     */
    public String convert(final String source, final boolean html) {
        return converter.convert(source, html);
    }
}
