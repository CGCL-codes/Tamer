package visugraph.lambda;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import visugraph.data.Attribute;
import visugraph.data.Data;
import visugraph.data.DataUtils;

/**
 * <p>Moteur de lambda qui permet de parser et générer des Lambdas ou des attributs.
 * Cette classe permet de définir le contexte nécessaire à la création des Lambdas. Elle permet
 * d'exposer les fonctions, attributs et constantes qui seront accessibles aux lambdas.</p>
 * 
 * <p>Une lambda est une fonction anonyme qui exécute des instructions et retourne un résultat
 * en fonction des paramètres qui lui sont donnés. Les lambdas peuvent être utilisées pour créer
 * de nouvelles fonctions à l'exécution ou faciliter l'utilisation des Attributs au sein de visugraph.</p>
 * 
 * <p>Les lambdas sont "statiquement" typées : une vérification du type des paramètres
 * et du type de retour est effectuée au moment de la compilation de la lambda. Notez également que : </p>
 * <ul>
 * 	<li>toutes les instructions sont sensibles à la casse.</li>
 * 	<li>la valeur "null" n'est pas autorisée.</li>
 * 	<li>les comparaisons d'égalité et d'inégalité se font via les méthodes .equals() des objets.</li>
 * </ul>
 * 
 * 
 * <h3>Structure d'une lambda</h3>
 * <p>Une lambda est composée de deux parties :</p>
 * <ul>
 * 	<li>Une liste de paramètres</li>
 * 	<li>Un corps de fonction</li>
 * </ul>
 * 
 * <pre>
 * Voici un exemple d'une lambda à deux paramètres réalisant un petit calcul mathématique :
 * (x,y) => 1/(sqrt(x)+y)
 * </pre>
 * 
 * <p>Les paramètres sont une liste de noms qui serviront à identifier les variables dans le corps de la fonction.
 * Ils sont placés entre parenthèses et séparés par une virgule. Une lambda peut ne pas contenir de paramètre.
 * Dans ce cas, la lambda commencera par une paire de parenthèses vide "()".</p>
 * 
 * <p>Le corps de la lambda est séparée des paramètres par l'opérateur "=>". Le corps de la fonction est
 * une suite d'instructions qui retourne obligatoirement une valeur. Ces instructions sont composées d'opérateurs,
 * de calculs, de fonctions, de constantes, de variables, ...</p>
 * 
 * <h3>Les litéraux supportés par les lambdas</h3>
 * <ul>
 * 	<li>Les chaînes de caractères sont placées au choix entre des guillements simples ou doubles : "une chaîne", 'une chaîne'.
 *  Des séquences d'échappements existent pour certains caractères : {@code \b, \n, \t, \r, \f, \", \', \\}</li>
 *  <li>Les entiers et les long s'écrivent de la même manière qu'en Java. Ils peuvent être écrit au choix
 *  en base 10, 16 ou 8. Ainsi {@code 0xFFF, 2334L ou 0777L} sont des nombres valides.</li>
 *  <li>Idem, les flottants et les doubles suivent les conventions d'écriture Java :
 *  {@code .001, 14.0f, -1e+45d} sont des nombres valides.</li>
 *  <li>Les valeurs booléennes  {@code true et false} (sensibles à la casse)</li>
 * </ul>
 * 
 * <h3>Les opérateurs supportés par les lambdas</h3>
 * <p>Tous les opérateurs donnés ci-dessous possèdent le même comportement que leur équivalent Java.
 * Ils respectent notamment les mêmes règles de précédence.</p>
 * 
 * <p>Opérateurs par thème</p>
 * <ul>
 * 	<li>Les opérateurs mathématiques : {@code *, +, -, /, % (modulo)}. Mêmes règles de typage pour les calculs que Java.
 * Ces opérateurs ne peuvent s'appliquer que sur des nombres.</li>
 *  <li>Les opérateurs relationnels : {@code ==, !=, >, <, >=, <=}. S'appliquent exclusivement sur des nombres à l'exception
 *  des opéraeurs {@code == et !=} qui peuvent s'appliquer sur n'importe quel type d'objets à l'aide de leur méthode {@link Object#equals equals}.</li>
 *  <li>Les opérateurs logiques : {@code &&, ||, !}</li>
 * </ul>
 * 
 * <p>Soit par précédence</p>
 * <ul>
 * 	<li>Unaire : <code>-, !</code></li>
 * 	<li>Multiplicatif : <code>*, /, %</code></li>
 * 	<li>Additif : <code>+, -</code></li>
 * 	<li>Relationnel : <code>&gt;, &lt;, &gt;=, &lt;=</code></li>
 * 	<li>Egalité : <code>==, !=</code></li>
 * 	<li>Logique ET : <code>&amp;&amp;</code></li>
 * 	<li>Logique OU : <code>||</code></li>
 * </ul>
 * 
 * <h3>Les fonctions, attributs et constantes</h3>
 * <p>Les fonctions et constantes peuvent être ajoutées au LambdaEngine. Par défaut, le LambdaEngine
 * intègre les fonctions des bibliothèques {@link MathFunctions}, {@link ColorFunctions},
 * {@link StringFunctions} et {@link GeomFunctions}.</p>
 * 
 * <p>Les {@link Attribute} sont transformés en fonction à un argument par le LambdaEngine.  La fonction prendra
 * le nom de l'attribut et seule la méthode {@code get()} sera disponible. Les attributs étant mutables
 * par nature, le LambdaEngine peut écouter les changements intervenant sur ses éléments. Pour plus de détails sur 
 * cette faculté, se reporter à {@link #addAttribute}.</p>
 * 
 * <h3>Les structures conditionnelles</h3>
 * <p>Le LambdaEngine supporte les structures "if, then, else if" chaînées. Une telle structure est composée
 * de deux parties et s'écrit entre crochets "[]". Un exemple : </p>
 * <pre>
 * (x) => [x >= 0, x < 0] [x, -x]
 * </pre>
 * <p>La lambda ci-dessus est équivalente à une valeur absolue. La première partie entre crochets définie
 * les conditions, et la deuxième, l'expression à renvoyer pour chaque condition. Il n'y a pas d'équivalent
 * au "else" final comme en Java. Il peut toutefois être simulé en spécifiant "true" comme dernière condition.</p> 
 */
public class LambdaEngine {

    private final Set<Constant> consts = new LinkedHashSet<Constant>();

    private final Set<Function> funcs = new LinkedHashSet<Function>();

    private final Map<Attribute<?, ?>, Function> attrs = new LinkedHashMap<Attribute<?, ?>, Function>();

    /**
	 * Créer un nouveau LambdaEngine. Il contiendra les bibliothèques de fonctions de
	 * {@link MathFunctions}, {@link ColorFunctions}, {@link StringFunctions} et {@link GeomFunctions}.
	 */
    public LambdaEngine() {
        MathFunctions.addAllToEngine(this);
        ColorFunctions.addAllToEngine(this);
        StringFunctions.addAllToEngine(this);
        GeomFunctions.addAllToEngine(this);
    }

    /**
	 * Ajoute une nouvelle fonction au LambdaEngine. Si une fonction avec un nom similaire existait déjà,
	 * elle sera remplacée par la nouvelle.
	 */
    public void addFunction(Function func) {
        this.funcs.add(func);
    }

    /**
	 * Supprime une fonction du LambdaEngine.
	 */
    public void removeFunction(Function func) {
        this.funcs.remove(func);
    }

    /**
	 * Supprime toutes les fonctions du LambdaEngine.
	 */
    public void clearFunctions() {
        this.funcs.clear();
    }

    /**
	 * Ajoute une nouvelle constante au LambdaEngine. Si une constante avec un nom similaire existait déjà,
	 * elle sera remplacée par la nouvelle.
	 */
    public void addConstant(Constant constant) {
        this.consts.add(constant);
    }

    /**
	 * Ajoute une nouvelle constante au LambdaEngine.
	 */
    public void addConstant(String name, Object value) {
        this.consts.add(new FinalConst(name, value));
    }

    /**
	 * Importe toutes les constantes d'un enum dans le LambdaEngine.
	 */
    public void importConstants(Class<? extends Enum<?>> clEnum) {
        for (Enum<?> oneElem : clEnum.getEnumConstants()) {
            this.consts.add(new FinalConst(oneElem.name(), oneElem));
        }
    }

    /**
	 * Supprime une constante du LambdaEngine.
	 */
    public void removeConstant(Constant constant) {
        this.consts.remove(constant);
    }

    /**
	 * Supprime toutes les constantes du LambdaEngine.
	 */
    public void clearConstants() {
        this.consts.clear();
    }

    /**
	 * Ajoute un nouvel attribut. L'attribut sera mis à disposition aux lambdas sous forme de fonction à un argument
	 * correspondant à un appel à la méthode {@link visugraph.data.Attribute#get}.
	 * Le nom de la fonction sera le même que celui de l'attribut.
	 * Si un attribut ou une fonction avec un nom similaire existait déjà,
	 * il sera écrasé par le nouvel attribut.
	 */
    public void addAttribute(Attribute<?, ?> attr) {
        if (this.attrs.containsKey(attr)) {
            return;
        }
        this.attrs.put(attr, new AttrFunc(attr));
    }

    /**
	 * Ajoute une nouvelle Data. 
	 * @param data la donnée à ajouter
	 * @param name le nom que prendra la donnée
	 * @param type le type des éléments retournés par la donnée.
 	 * @see #addAttribute(Attribute)
	 */
    public <K, V> void addAttribute(Data<K, V> data, String name, Class<V> type) {
        this.addAttribute(DataUtils.toAttribute(data, name, type));
    }

    /**
	 * Supprime un attribut.
	 */
    public void removeAttribute(Attribute<?, ?> attr) {
        this.attrs.remove(attr);
    }

    /**
	 * Supprime tous les attributs.
	 */
    public void clearAttributes() {
        this.attrs.clear();
    }

    /**
	 * Compile une expression en Lambda. Pour compiler, la liste des types
	 * des paramètres doit également être fournie. Elle permet entre autre
	 * de déterminer le type de valeur retournée par la lambda et assurer
	 * le typage statique de la fonction.
	 * @param expr l'expression à compiler
	 * @param paramTypes le type des paramètres
	 * @throws SyntaxException si la syntaxe de l'expression est incorrecte
	 */
    public Lambda compile(String expr, Class<?>... paramTypes) {
        LambdaParser parser = this.createParser();
        Expression rootExpr = parser.parse(expr, Arrays.asList(paramTypes));
        List<ParameterExpression> paramExprs = new ArrayList<ParameterExpression>(parser.params.values());
        return new Lambda(rootExpr, paramExprs);
    }

    /**
	 * <p>Compile une expression sous forme d'attribut. L'attribut retourné sera en lecture
	 * seulement. Si l'expression elle même fait usage d'autres attributs (passés via {@link #addAttribute}),
	 * ils seront convenablement écoutés et tout évènement survenant sur l'un d'eux sera propagé.</p>
	 * 
	 * @param expr l'expression à compiler
	 * @param keyType le type de clé de l'attribut. Correspond au premier paramètre de la lambda.
	 * @param valueType le type des valeurs attendue pour l'attribut. Correspond au type de retour de la lambda.
	 * @throws SyntaxException si la syntaxe de l'expression est incorrecte.
	 * @throws ClassCastException si le type retourné par la lambda ne correspond pas au type attendu.
	 */
    public <K, V> Attribute<K, V> compileAttribute(String expr, Class<? extends K> keyType, Class<? extends V> valueType) {
        LambdaParser parser = this.createParser();
        Expression rootExpr = parser.parse(expr, Arrays.asList(new Class<?>[] { keyType }));
        ParameterExpression paramExpr = parser.params.values().iterator().next();
        Class<?> returnType = rootExpr.getReturnType();
        if (!valueType.isAssignableFrom(returnType)) {
            throw new SyntaxException("Le type de retour de la lambda est incorrect. " + "Type attendu : " + valueType.getSimpleName() + ", Type trouvé : " + returnType.getSimpleName());
        }
        LambdaAttribute<K, V> lambdaAttr = new LambdaAttribute<K, V>(expr, rootExpr, paramExpr);
        for (Function oneFunc : parser.usedFunctions) {
            if (oneFunc instanceof AttrFunc) {
                ((AttrFunc) oneFunc).attr.addAttributeListener(lambdaAttr);
            }
        }
        return lambdaAttr;
    }

    private LambdaParser createParser() {
        List<Function> allFuncs = new ArrayList<Function>(this.funcs.size() + this.attrs.size());
        allFuncs.addAll(this.funcs);
        allFuncs.addAll(this.attrs.values());
        return new LambdaParser(allFuncs, this.consts);
    }

    /**
	 * Petite classe qui permet de transformer l'appel d'un Attribut en appel de fonction Function
	 * reconaissable par une lambda.
	 */
    private static class AttrFunc implements Function {

        private static final List<Class<?>> PARAMS = Arrays.asList(new Class<?>[] { Object.class });

        protected Attribute<Object, Object> attr;

        @SuppressWarnings("unchecked")
        public AttrFunc(Attribute<?, ?> attr) {
            this.attr = (Attribute) attr;
        }

        public Object exec(List<Object> params) {
            return this.attr.get(params.get(0));
        }

        public String getName() {
            return this.attr.getName();
        }

        public Class<?> getReturnType() {
            return this.attr.getType();
        }

        public boolean isVarArgs() {
            return false;
        }

        public List<Class<?>> getParameters() {
            return PARAMS;
        }
    }

    /**
	 * Implémentation par défaut pour une constante.
	 */
    private static class FinalConst implements Constant {

        private final String name;

        private final Object value;

        public FinalConst(String name, Object value) {
            this.name = name;
            this.value = value;
        }

        public String getName() {
            return this.name;
        }

        public Object getValue() {
            return this.value;
        }
    }
}
