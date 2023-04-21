package astcentric.structure.vl.basic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import astcentric.structure.basic.Node;
import astcentric.structure.bl.AbstractCalculator;
import astcentric.structure.bl.CalculationContext;
import astcentric.structure.bl.Calculator;
import astcentric.structure.bl.CalculatorBasedFunction;
import astcentric.structure.bl.CharacterData;
import astcentric.structure.bl.ConstructorDeclaration;
import astcentric.structure.bl.Data;
import astcentric.structure.bl.DataFactory;
import astcentric.structure.bl.Function;
import astcentric.structure.bl.FunctionDeclaration;
import astcentric.structure.bl.StringData;

class PrimitiveFunctionsManager {

    private static final class StringToCharacterArray extends AbstractCalculator {

        private final Node _arrayNode;

        private final Compiler _compiler;

        private final DataFactory<Object> _dataFactory;

        private ConstructorDeclaration _arrayConstructor;

        StringToCharacterArray(Resources resources) {
            BasicVLNodes nodes = resources.getNodes();
            _compiler = resources.getCompiler();
            _dataFactory = resources.getDataFactory();
            _arrayNode = BasicDataTypes.ARRAY.getDefinitionNode(nodes);
        }

        protected Data doCalculate(CalculationContext calculationContext, List<Function> arguments) {
            if (arguments.size() != 1) {
                throw new IllegalArgumentException("Exact one argument expected instead of " + arguments.size());
            }
            Function function = arguments.get(0);
            if (function instanceof StringData == false) {
                throw new IllegalArgumentException("Not a string data: " + function);
            }
            StringData stringData = (StringData) function;
            String string = stringData.getString();
            List<Data> characters = new ArrayList<Data>(string.length());
            for (int i = 0, n = string.length(); i < n; i++) {
                char c = string.charAt(i);
                characters.add(_dataFactory.create(c));
            }
            return new Data(getArrayConstructor(), characters);
        }

        private ConstructorDeclaration getArrayConstructor() {
            if (_arrayConstructor == null) {
                _arrayConstructor = _compiler.compileDataTypeDeclaration(_arrayNode).getConstructorDeclarations().get(0);
            }
            return _arrayConstructor;
        }
    }

    private static final class CharacterArrayToString extends AbstractCalculator {

        private final DataFactory<Object> _dataFactory;

        CharacterArrayToString(Resources resources) {
            _dataFactory = resources.getDataFactory();
        }

        protected Data doCalculate(CalculationContext calculationContext, List<Function> arguments) {
            if (arguments.size() != 1) {
                throw new IllegalArgumentException("Exact one argument expected instead of " + arguments.size());
            }
            Function argument = arguments.get(0);
            if (argument instanceof Data == false) {
                throw new IllegalArgumentException("Not a data argument: " + argument);
            }
            Data data = (Data) argument;
            List<Data> children = data.getChildren();
            StringBuilder builder = new StringBuilder();
            for (Data child : children) {
                if (child instanceof CharacterData == false) {
                    throw new IllegalArgumentException((builder.length() + 1) + " array element isn't a character data: " + data);
                }
                CharacterData characterData = (CharacterData) child;
                builder.append(characterData.getCharacter());
            }
            String result = builder.toString();
            return _dataFactory.create(result);
        }
    }

    static PrimitiveFunctionsManager createAndPrepare(Resources resources) {
        PrimitiveFunctionsManager manager = new PrimitiveFunctionsManager(resources);
        manager.register(BasicVLFunctions.COMPILE_DATA_NODE, new CompileDataNodeCalculator(resources));
        manager.register(BasicVLFunctions.TRAVERSE_NODES, new TraverseNodesCalculator(resources));
        manager.register(BasicVLFunctions.GET_NODE_NAME, new GetNodeNameCalculator(resources));
        manager.register(BasicVLFunctions.GET_NODE_VALUE, new GetNodeValueCalculator(resources));
        manager.register(BasicVLFunctions.CONVERT_NODE_TO_NODE_COLLECTION, new ConvertNodeToNodeCollectionCalculator(resources));
        manager.register(BasicVLFunctions.GET_REFERENCE_OR_DEFAULT, new GetReferenceCalculator());
        manager.register(BasicVLFunctions.CONVERT_TO_SEQUENCE_OF_MATCHERS, new ConverterToMatchersCalculator(resources, 1));
        manager.register(BasicVLFunctions.CONVERT_TO_ALTERNATIVE_OF_MATCHERS, new ConverterToMatchersCalculator(resources, 2));
        register(manager, NodeCollectionToNodeCollectionFunctionDefinition.values(), resources);
        register(manager, TwoNodeCollectionsToNodeCollectionFunctionDefinition.values(), resources);
        manager.register(MiscelleneousNodeCollectionFunctionNodes.CREATE_FILTERED_NODE_COLLECTION, new CreateFilteredNodeCollectionCalculator(resources));
        manager.register(MiscelleneousNodeCollectionFunctionNodes.CONTAINS_NODE, new ContainsNodeCalculator(resources));
        manager.register(BasicVLFunctions.APPLY_REGEX, new RegExCalculator(resources));
        manager.register(BasicFunctions.STRING_TO_CHAR_ARRAY, new StringToCharacterArray(resources));
        manager.register(BasicFunctions.CHAR_ARRAY_TO_STRING, new CharacterArrayToString(resources));
        return manager;
    }

    private static void register(PrimitiveFunctionsManager manager, CalculatorFactoryWithNodeResolver[] defs, Resources resources) {
        for (int i = 0, n = defs.length; i < n; i++) {
            CalculatorFactoryWithNodeResolver def = defs[i];
            Calculator function = def.createCalculator(resources.getDataFactory());
            manager.register(def, function);
        }
    }

    private final Compiler _compiler;

    private final BasicVLNodes _nodes;

    private final Map<Node, Calculator> _calculators;

    PrimitiveFunctionsManager(Resources resources) {
        _compiler = resources.getCompiler();
        _nodes = resources.getNodes();
        _calculators = new HashMap<Node, Calculator>();
    }

    void register(DefinitionNodeResolver resolver, Calculator calculator) {
        _calculators.put(resolver.getDefinitionNode(_nodes), calculator);
    }

    Function compile(Node node) {
        final Calculator calculator = _calculators.get(node);
        if (calculator == null) {
            return null;
        }
        FunctionDeclaration declaration = _compiler.compileFunctionDeclaration(node);
        return new CalculatorBasedFunction(declaration, calculator);
    }
}
