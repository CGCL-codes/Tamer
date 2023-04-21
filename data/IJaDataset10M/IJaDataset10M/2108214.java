package exception;

/**
 *
 * @author w4m
 */
public class NoUnitPriceDefinedException extends Exception {

    public NoUnitPriceDefinedException(String service) {
        super("Defina um preço unitário para o serviço: " + service);
    }
}
