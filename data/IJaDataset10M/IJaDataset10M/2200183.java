package be.vanvlerken.bert.zfpricemgt.database.importers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import be.vanvlerken.bert.zfpricemgt.IProduct;
import be.vanvlerken.bert.zfpricemgt.database.Product;

/**
 * Imports the PDF based ZF data, as provided in 2004
 */
public class Pdf2004Importer implements Importer {

    private static final ResourceBundle msgs = ResourceBundle.getBundle("be.vanvlerken.bert.zfpricemgt.database.importers.localization.Pdf2004Importer");

    private BufferedReader reader;

    private Date validSince;

    private final Pattern detectPattern;

    private final Pattern parsePattern;

    public Pdf2004Importer() {
        detectPattern = Pattern.compile("^.{4} .{3} .{3} [\\w ]+ 01 (\\d* )?\\d+\\.\\d{2} � $");
        parsePattern = Pattern.compile("^(.{4} .{3} .{3}) ([\\w ]+) 01 ((\\d* )?\\d+\\.\\d{2}) � $");
    }

    /**
     * @see be.vanvlerken.bert.zfpricemgt.database.importers.Importer#canImport(java.io.InputStream)
     */
    public boolean canImport(InputStream is) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        try {
            for (int i = 0; i < 5; i++) {
                reader.readLine();
            }
            String line = reader.readLine();
            return detectPattern.matcher(line).matches();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * @param line
     * @return
     */
    private IProduct parseProduct(String line, Date validSince) {
        String productNumber = null;
        String description = null;
        double price = 0.0;
        Matcher matcher = parsePattern.matcher(line);
        if (!matcher.find()) {
            return null;
        }
        productNumber = matcher.group(1);
        description = matcher.group(2);
        String priceStr = matcher.group(3);
        priceStr = priceStr.replace(" ", "");
        price = Double.parseDouble(priceStr);
        return new Product(productNumber, description, price, validSince);
    }

    public String toString() {
        return msgs.getString("description");
    }

    public void startImport(InputStream is, Date validSince, boolean overruleValidSince) {
        reader = new BufferedReader(new InputStreamReader(is));
        this.validSince = validSince;
    }

    public IProduct nextProduct() {
        String line;
        try {
            line = reader.readLine();
            while (line != null) {
                IProduct newProduct = parseProduct(line, validSince);
                if (newProduct != null) {
                    return newProduct;
                }
                line = reader.readLine();
            }
        } catch (IOException e) {
        }
        return null;
    }

    public void stopImport() {
        reader = null;
        validSince = null;
    }
}
