package examples.controllers;

import prest.core.Controller;
import prest.core.annotations.Action;
import prest.core.annotations.Doc;
import prest.core.annotations.Key;
import prest.core.types.RequestParameters;
import prest.core.types.UrlParameters;
import prest.json.annotations.Json;
import prest.web.annotations.View;

/**
 * <p>
 * Kontroler musi extendovat triedu prest.core.Controller. Kontroler namapujeme
 * na cast URL v aplikacnej triede:
 *
 * <pre>
 * package quickstart;
 *
 * public class QuickStartApplication extends prest.core.Application {
 * 	public void initialize() throws ApplicationException {
 * 		mount(&quot;quick_start&quot;, new QuickStart());
 * 	}
 * }
 * </pre>
 *
 * Tuto triedu zavedieme do web.xml:
 *
 * <pre>
 * &lt;filter&gt;
 * 	&lt;filter-name&gt;ServletFilter&lt;/filter-name&gt;
 * 	&lt;filter-class&gt;prest.core.ServletFilter&lt;/filter-class&gt;
 * 	&lt;init-param&gt;
 * 		&lt;param-name&gt;Application&lt;/param-name&gt;
 * 		&lt;param-value&gt;quickstart.QuickStartApplication&lt;/param-value&gt;
 * 	&lt;/init-param&gt;
 * &lt;/filter&gt;
 * &lt;filter-mapping&gt;
 * 	&lt;filter-name&gt;ServletFilter&lt;/filter-name&gt;
 * 	&lt;url-pattern&gt;/*&lt;/url-pattern&gt;
 * &lt;/filter-mapping&gt;
 * </pre>
 *
 * Metody kontrolera mozme volat na URL:
 *
 * http://&lt;host&gt;:&lt;port&gt;/&lt;app_name&gt;/quick_start/&lt;action_name
 * &gt;
 * <ul>
 * <li>&lt;app_name&gt; - meno aplikacie (context root)</li>
 * <li>&lt;action_name&gt; - meno volanej metody</li>
 * </ul>
 * </p>
 */
public class QuickStart extends Controller {

    /**
	 * String vrateny touto metodou je zapisany na vystup.
	 *
	 * Pouzitie anotacie Action s neuvedenym menom znamena, ze tato metoda sa
	 * zavola, ak v URL za kontrolerom nebude uvedene ziadne ine meno metody.
	 *
	 * Anotacia Doc sluzi na dokumentaciu kontrolera, metody (akcie) a parametra
	 * metody - kontroler vystavuje svoju dokumentaciu pomocou metod _docs_ a
	 * _doc_.
	 *
	 * @return retazec zapisany na vystup
	 */
    @Doc("Dokumentacia metody")
    @Action
    public String hello_world() {
        return "<html><body>Hello World!</body></html>";
    }

    /**
	 * POST/GET parametre s klucami p1 a p2 su namapovane na parametre param1 a
	 * param2 pouzitim anotacie Key.
	 *
	 * @param param1
	 *            hodnota POST/GET parametru p1 je namapovana do tohto parametra
	 * @param param2
	 *            hodnota POST/GET parametru p2 je namapovana do tohto parametra
	 */
    @Action(name = "postget_parameter")
    public void postget_parameter(@Key("p1") String param1, @Key("p2") String param2) {
    }

    /**
	 * Specialne typy UrlParameters a RequestParameters zapuzdruju vsetky
	 * poslane URL resp. POST/GET parametre.
	 *
	 * @param urlParameters
	 *            vsetky URL parametre.
	 * @param requestParameters
	 *            vsetky POST/GET parametre.
	 */
    @Action(name = "special_parameters")
    public void special_parameters(UrlParameters urlParameters, RequestParameters requestParameters) {
    }

    /**
	 * Object, ktory vrati tato metoda, je serializovany do JSON formatu
	 * pouzitim anotacie Json.
	 *
	 * @param id
	 *            URL parameter
	 * @return objekt, ktory bude serializovany do JSON formatu pouzitim
	 *         anotacie Json.
	 */
    @Action(name = "test_json")
    @Json
    public Car test_json(Long id) {
        Car car = Car.get(id);
        return car;
    }

    /**
	 * Metoda vracia nejake data, ktorych zobrazenie deleguje na definovanu JSP
	 * stranku pouzitim anotacie View
	 *
	 * @return data, ktore zobrazi definovana JSP stranka
	 */
    @Doc("Priklad pouzitia View anotacie")
    @Action(name = "test_view")
    @View(template = "/templates/view.jsp")
    public Object test_view() {
        return "data to view";
    }

    /**
	 * URL parametre su postupne namapovane do argumentov tejto metody (kedze
	 * nie je pouzita anotacia Key)
	 *
	 * @param stringParam
	 *            hodnota prveho URL parametra je namapovana do tohto parametra
	 * @param integerParam
	 *            hodnota druheho URL parametra je namapovana do tohto
	 *            parametra, v pripade neuspesnej konverzie retazca na cele
	 *            cislo ma hodnotu null
	 */
    @Action(name = "url_parameter")
    public void url_parameter(@Doc("Dokumentacia parametru") String stringParam, Integer integerParam) {
    }
}

class Car {

    public static Car get(Long id) {
        return new Car(id);
    }

    private final long id;

    public Car(long id) {
        this.id = id;
    }

    public long getId() {
        return this.id;
    }
}
