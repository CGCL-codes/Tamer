package eu.irreality.age;

public interface Output {

    /**
	 * @deprecated Use {@link #write(String)} instead
	 */
    public void escribir(String s);

    public void write(String s);
}
