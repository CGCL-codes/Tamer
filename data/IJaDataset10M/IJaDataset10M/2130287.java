package sk.sigp.tetras.itext;

import java.util.EnumMap;

public interface IPdfFormFillService {

    String FIELD_COMPANY_NAME = "nazov";

    String FIELD_COMPANY_ADDRESS = "adresa";

    String FIELD_COMPANY_COUNTRY = "krajina";

    String FIELD_COMPANY_EMAIL = "email";

    String FIELD_COMPANY_FAX = "fax";

    String FIELD_COMPANY_PHONE = "telefon";

    String FIELD_COMPANY_WEB = "web";

    String FIELD_COMPANY_DATE = "datum";

    byte[] fillPdfForm(EnumMap<PdfParamEnum, String> aFillData, byte[] aPdfToFill) throws PdfFillingException;
}
