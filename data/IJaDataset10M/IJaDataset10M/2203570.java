package mx.ipn.to;

public class TransferObject {

    private int tipoError;

    private String descTipoError;

    private String error;

    public static final int ERROR = 1;

    public static final int ADVERTENCIA = 2;

    public static final int INFO = 3;

    public static final int CONFIRMACION = 4;

    public TransferObject() {
        super();
        error = null;
        tipoError = 0;
    }

    public void setTipoError(int tipoError) {
        this.tipoError = tipoError;
        switch(tipoError) {
            case 0:
                descTipoError = "";
                break;
            case 1:
                descTipoError = "Error";
                break;
            case 2:
                descTipoError = "Advertencia";
                break;
            case 3:
                descTipoError = "Información";
                break;
            case 4:
                descTipoError = "Confirmación";
                break;
        }
    }

    public int getTipoError() {
        return tipoError;
    }

    public String getDescTipoError() {
        return descTipoError;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getError() {
        return error;
    }

    public TransferObject(int tipoError, String descTipoError, String error) {
        super();
        this.tipoError = tipoError;
        this.descTipoError = descTipoError;
        this.error = error;
    }
}
