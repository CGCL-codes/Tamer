package deesel.parser.com.nodes;

import deesel.lang.module.Module;
import deesel.lang.module.ModuleLocator;
import deesel.parser.ASTNode;
import deesel.parser.com.COMNode;
import deesel.parser.com.exceptions.COMIntegrityException;
import deesel.parser.com.exceptions.CannotAddChildOfThisTypeException;
import deesel.parser.com.util.ClassDefFactory;
import deesel.parser.com.visitor.COMVisitor;
import deesel.parser.com.visitor.VisitorContext;
import deesel.parser.exceptions.IncompatibleVersionException;
import deesel.util.logging.Logger;
import java.math.BigDecimal;
import java.util.Arrays;

/**
 * The Requires node allows a Class/Module author to define pre-requisites
 * Modules for the Class. Strictly speaking the requires belongs to a
 * compilation unit rather than a class.
 *
 * @author <a href="mailto:neil.ellis@mangala.co.uk>Neil Ellis</a>
 * @version $Revision: 1.1 $
 * @todo It is intended that these requirements will eventually be persisted
 * with the compilation unit.
 */
public class Requires extends AbstractCOMNode {

    private static Logger log = Logger.getLogger(Requires.class);

    private FloatLiteral version;

    private Operator comparisonOperator;

    private DeeselPackage deeselPackage;

    public static final String STATIC_FIELD_NAME = "__G_REQUIRES";

    public Requires(ASTNode node) {
        super(node);
    }

    /**
     * This constructor is to allow the Requires nodes to be serialized with the
     * class.
     *
     * @param sPackage  the package of the required module as a String array
     * @param sOperator the operator used as a String (or null)
     * @param sVersion  the version required as a String (or null)
     */
    public Requires(String[] sPackage, String sOperator, String sVersion) {
        super(null);
        if (sPackage != null) {
            add(new DeeselPackage(null, Arrays.asList(sPackage)));
            add(new Operator(sOperator));
            add(new FloatLiteral(new BigDecimal(sVersion)));
        }
    }

    public void accept(COMVisitor visitor, VisitorContext context) {
        visitor.visit(this, context);
    }

    public void addChild(FloatLiteral version) throws CannotAddChildOfThisTypeException {
        assertNotNull(version, "Cannot add a null version to a requires statement.");
        assertIsNull(this.version, "Cannot add more than one version to a requires statement.");
        this.version = version;
    }

    public void addChild(Operator operator) throws CannotAddChildOfThisTypeException {
        assertNotNull(operator, "Cannot add a null operator to a requires statement.");
        assertIsNull(this.comparisonOperator, "Cannot add more than one operator to a requires statement.");
        this.comparisonOperator = operator;
    }

    public void addChild(DeeselPackage deeselPackage) throws CannotAddChildOfThisTypeException {
        assertNotNull(deeselPackage, "Cannot add a null pacakge to a requires statement.");
        assertIsNull(this.deeselPackage, "Cannot add more than one package to a requires statement.");
        this.deeselPackage = deeselPackage;
    }

    public DeeselClass getType() {
        return ClassDefFactory.VOID;
    }

    public COMNode[] getAllChildren() {
        return new COMNode[] { deeselPackage, version, comparisonOperator };
    }

    public void setVersion(FloatLiteral version) {
        this.version = version;
    }

    public void setComparison(Operator comparison) {
        this.comparisonOperator = comparison;
    }

    public FloatLiteral getVersion() {
        return version;
    }

    public Operator getComparisonOperator() {
        return comparisonOperator;
    }

    /**
     * We must now check to see if the module that this compilation unit says it
     * requires is actually available.
     *
     * @throws COMIntegrityException
     */
    public void validate() throws COMIntegrityException {
        Module module = ModuleLocator.getModuleByPackage(deeselPackage);
        float moduleVersion = module.getModuleMetadata().getVersion();
        float statedVersion = version.getValue().floatValue();
        if (comparisonOperator.equals(Operator.LESS_THAN)) {
            if (moduleVersion >= version.getValue().floatValue()) {
                throw new IncompatibleVersionException(this, moduleVersion, deeselPackage, comparisonOperator, statedVersion);
            }
        } else if (comparisonOperator.equals(Operator.LESS_THAN_OR_EQUAL)) {
            if (moduleVersion > version.getValue().floatValue()) {
                throw new IncompatibleVersionException(this, moduleVersion, deeselPackage, comparisonOperator, statedVersion);
            }
        } else if (comparisonOperator.equals(Operator.GREATER_THAN)) {
            if (moduleVersion <= version.getValue().floatValue()) {
                throw new IncompatibleVersionException(this, moduleVersion, deeselPackage, comparisonOperator, statedVersion);
            }
        } else if (comparisonOperator.equals(Operator.GREATER_THAN_OR_EQUAL)) {
            if (moduleVersion < version.getValue().floatValue()) {
                throw new IncompatibleVersionException(this, moduleVersion, deeselPackage, comparisonOperator, statedVersion);
            }
        } else if (comparisonOperator.equals(Operator.NOT_EQUAL)) {
            if (moduleVersion == version.getValue().floatValue()) {
                throw new IncompatibleVersionException(this, moduleVersion, deeselPackage, comparisonOperator, statedVersion);
            }
        } else if (comparisonOperator.equals(Operator.IDENTITY)) {
            if (moduleVersion != version.getValue().floatValue()) {
                throw new IncompatibleVersionException(this, moduleVersion, deeselPackage, comparisonOperator, statedVersion);
            }
        }
    }

    public Module getModule() {
        return ModuleLocator.getModuleByPackage(deeselPackage);
    }

    public DeeselPackage getPackage() {
        return deeselPackage;
    }
}
