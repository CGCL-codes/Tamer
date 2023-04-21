package ramon;

/**
 * Interfaz que proporciona un punto de extensión para, ante una respuesta de
 * redirección, poder producir el evento que posteriormente se destruirá para
 * poner los parámetros a la URL de redirección
 */
public interface RedirectHandler {

    /**
	 * Método que tienen que definir las clases implementen esta interfaz.
	 * 
	 * @param ctx
	 *            El contexto de ejecución (sesión y acción, para sacar datos de
	 *            ahí
	 * @param att
	 *            Los atributos resultado de la acción, para sacar datos también
	 *            de ahí
	 * @return el evento para destruir
	 */
    Evento handle(Contexto ctx, Atributos att);
}
