package example.java;

import pobs.*;
import pobs.control.PLongestAlternative;
import pobs.parser.PChar;
import pobs.parser.PKleene;
import pobs.parser.PList;
import pobs.parser.PMultiple;
import pobs.parser.POptional;
import pobs.parser.POr;
import pobs.parser.PObjectPointer;
import pobs.parser.PSequence;

/**
 * Java syntax parser
 * 
 * @author Martijn W. van der Lee
 */
public class Java extends PParser {

    private PObject parser;

    private PObject skipper = new JavaSkipper();

    private JavaIdentifier identifier = new JavaIdentifier();

    private JavaIdentifier literal = new JavaIdentifier();

    public PObjectPointer ptr_array_initializer = new PObjectPointer();

    public PObjectPointer ptr_type = new PObjectPointer();

    public PObjectPointer ptr_block = new PObjectPointer();

    public PObjectPointer ptr_block_statements = new PObjectPointer();

    public PObjectPointer ptr_statement = new PObjectPointer();

    public PObjectPointer ptr_statement_no_short_if = new PObjectPointer();

    public PObjectPointer ptr_expression = new PObjectPointer();

    public PObjectPointer ptr_assignment_expression = new PObjectPointer();

    public PObjectPointer ptr_conditional_expression = new PObjectPointer();

    public PObjectPointer ptr_conditional_or_expression = new PObjectPointer();

    public PObjectPointer ptr_conditional_and_expression = new PObjectPointer();

    public PObjectPointer ptr_inclusive_or_expression = new PObjectPointer();

    public PObjectPointer ptr_exclusive_or_expression = new PObjectPointer();

    public PObjectPointer ptr_and_expression = new PObjectPointer();

    public PObjectPointer ptr_equality_expression = new PObjectPointer();

    public PObjectPointer ptr_relational_expression = new PObjectPointer();

    public PObjectPointer ptr_shift_expression = new PObjectPointer();

    public PObjectPointer ptr_additive_expression = new PObjectPointer();

    public PObjectPointer ptr_multiplicative_expression = new PObjectPointer();

    public PObjectPointer ptr_unary_expression = new PObjectPointer();

    public PObjectPointer ptr_cast_expression = new PObjectPointer();

    public PObjectPointer ptr_postfix_expression = new PObjectPointer();

    public PObjectPointer ptr_field_access = new PObjectPointer();

    public PObjectPointer ptr_method_invocation = new PObjectPointer();

    public PObjectPointer ptr_primary_no_new_array = new PObjectPointer();

    public PObjectPointer ptr_package_declaration = new PObjectPointer();

    public PObjectPointer ptr_import_declarations = new PObjectPointer();

    public PObjectPointer ptr_type_declarations = new PObjectPointer();

    /**
     * Java constructor comment. Initializes the Java parser following the Java
     * Syntax Specification
     */
    public Java() {
        super();
        PObject c_parenL = new PChar('(');
        PObject c_parenR = new PChar(')');
        PObject c_blockL = new PChar('[');
        PObject c_blockR = new PChar(']');
        PObject c_accolL = new PChar('{');
        PObject c_accolR = new PChar('}');
        PObject c_colon = new PChar(':');
        PObject c_semicolon = new PChar(';');
        PObject c_comma = new PChar(',');
        PObject c_dot = new PChar('.');
        PObject c_star = new PChar('*');
        PObject simple_type_name = identifier;
        PObject package_name = new PList(identifier, c_dot, 1, PList.INFINITE);
        PObject type_name = package_name;
        PObject expression_name = package_name;
        PObject method_name = package_name;
        PObject interface_type = type_name;
        PObject class_type = type_name;
        PObject class_type_list = new PList(class_type, c_comma);
        PObject throws_declaration = new PSequence(new JavaToken("throws"), class_type_list);
        PObject variable_declarator_id = new PSequence(identifier, new PKleene(new PSequence(c_blockL, c_blockR)));
        PObject formal_parameter = new PSequence(ptr_type, variable_declarator_id);
        PObject formal_parameter_list = new PList(formal_parameter, c_comma);
        PObject variable_initializer = new POr(ptr_expression, ptr_array_initializer);
        PObject variable_initializers = new PMultiple(variable_initializer);
        PObject array_initializer = new PSequence(c_accolL, new PList(variable_initializers, c_comma), c_accolR);
        ptr_array_initializer.set(array_initializer);
        PObject variable_declarator = new PSequence(variable_declarator_id, new POptional(new PSequence(new PChar('='), variable_initializer)));
        PObject variable_declarators = new PList(variable_declarator, c_comma);
        PObject class_or_interface_type = new POr(class_type, interface_type);
        PObject floating_point_type = new JavaTokens("float", "double");
        PObject integral_type = new JavaTokens("byte", "short", "int", "long", "char");
        PObject numeric_type = new POr(integral_type, floating_point_type);
        PObject primitive_type = new POr(numeric_type, new JavaToken("boolean"));
        PObject dims = new PMultiple(new PSequence(c_blockL, c_blockR));
        PObject array_type = new PSequence(primitive_type, dims);
        PObject reference_type = new POr(class_or_interface_type, array_type).addControl(new PLongestAlternative());
        PObject type = new POr(primitive_type, reference_type).addControl(new PLongestAlternative());
        ptr_type.set(type);
        PObject array_access = new PSequence(new POr(expression_name, ptr_primary_no_new_array), c_blockL, ptr_expression, c_blockR);
        PObject dim_expr = new PSequence(c_blockL, ptr_expression, c_blockR);
        PObject dim_exprs = new PMultiple(dim_expr);
        PObject array_creation_expression = new PSequence(new JavaToken("new"), new POr(primitive_type, class_or_interface_type), dim_exprs, new POptional(dims));
        PObject argument_list = new PList(ptr_expression, c_comma);
        PObject class_instance_creation_expression = new PSequence(new JavaToken("new"), class_type, c_parenL, new POptional(argument_list), c_parenR);
        PObject primary_no_new_array = new POr(new PObject[] { literal, new JavaToken("this"), new PSequence(c_parenL, ptr_expression, c_parenR), class_instance_creation_expression, ptr_field_access, ptr_method_invocation, array_access });
        ptr_primary_no_new_array.set(primary_no_new_array);
        PObject primary = new POr(primary_no_new_array, array_creation_expression);
        PObject field_access = new PSequence(new POr(primary, new JavaToken("super")), c_dot, identifier);
        ptr_field_access.set(field_access);
        PObject method_invocation = new PSequence(new POr(method_name, new PSequence(new POr(primary, new JavaToken("super")), c_dot, identifier)), c_parenL, new POptional(argument_list), c_parenR);
        ptr_method_invocation.set(method_invocation);
        PObject postincrement_expression = new PSequence(ptr_postfix_expression, new JavaToken("++"));
        PObject postdecrement_expression = new PSequence(ptr_postfix_expression, new JavaToken("--"));
        PObject postfix_expression = new POr(primary, expression_name, postincrement_expression, postdecrement_expression);
        ptr_postfix_expression.set(postfix_expression);
        PObject unary_expression_not_plus_minus = new POr(postfix_expression, new PSequence(new PChar('~'), ptr_unary_expression), new PSequence(new PChar('!'), ptr_unary_expression), ptr_cast_expression);
        PObject preincrement_expression = new PSequence(new JavaToken("++"), ptr_unary_expression);
        PObject predecrement_expression = new PSequence(new JavaToken("--"), ptr_unary_expression);
        PObject unary_expression = new POr(preincrement_expression, predecrement_expression, new PSequence(new PChar('+'), ptr_unary_expression), new PSequence(new PChar('-'), ptr_unary_expression), unary_expression_not_plus_minus);
        ptr_unary_expression.set(unary_expression);
        PObject cast_expression = new POr(new PSequence(c_parenL, primitive_type, c_parenR, unary_expression), new PSequence(c_parenL, reference_type, c_parenR, unary_expression_not_plus_minus));
        ptr_cast_expression.set(cast_expression);
        PObject multiplicative_expression = new POr(unary_expression, new PSequence(ptr_multiplicative_expression, new PChar('*'), unary_expression), new PSequence(ptr_multiplicative_expression, new PChar('/'), unary_expression), new PSequence(ptr_multiplicative_expression, new PChar('%'), unary_expression));
        ptr_multiplicative_expression.set(multiplicative_expression);
        PObject additive_expression = new POr(multiplicative_expression, new PSequence(ptr_additive_expression, new PChar('+'), multiplicative_expression), new PSequence(ptr_additive_expression, new PChar('-'), multiplicative_expression));
        ptr_additive_expression.set(additive_expression);
        PObject shift_expression = new POr(additive_expression, new PSequence(ptr_shift_expression, new JavaToken("<<"), additive_expression), new PSequence(ptr_shift_expression, new JavaToken(">>"), additive_expression), new PSequence(ptr_shift_expression, new JavaToken(">>>"), additive_expression));
        ptr_shift_expression.set(shift_expression);
        PObject relational_expression = new POr(new PObject[] { shift_expression, new PSequence(ptr_relational_expression, new PChar('<'), shift_expression), new PSequence(ptr_relational_expression, new JavaToken(">"), shift_expression), new PSequence(ptr_relational_expression, new JavaToken("<="), shift_expression), new PSequence(ptr_relational_expression, new JavaToken(">="), shift_expression), new PSequence(ptr_relational_expression, new JavaToken("instanceof"), reference_type) });
        ptr_relational_expression.set(relational_expression);
        PObject equality_expression = new POr(relational_expression, new PSequence(ptr_equality_expression, new JavaToken("=="), relational_expression), new PSequence(ptr_equality_expression, new JavaToken("!="), relational_expression));
        ptr_equality_expression.set(equality_expression);
        PObject and_expression = new POr(equality_expression, new PSequence(ptr_and_expression, new PChar('&'), equality_expression));
        ptr_and_expression.set(and_expression);
        PObject exclusive_or_expression = new POr(and_expression, new PSequence(ptr_exclusive_or_expression, new PChar('^'), and_expression));
        ptr_exclusive_or_expression.set(exclusive_or_expression);
        PObject inclusive_or_expression = new POr(exclusive_or_expression, new PSequence(ptr_inclusive_or_expression, new PChar('|'), exclusive_or_expression));
        ptr_inclusive_or_expression.set(inclusive_or_expression);
        PObject conditional_and_expression = new POr(inclusive_or_expression, new PSequence(ptr_conditional_and_expression, new JavaToken("||"), inclusive_or_expression));
        ptr_conditional_and_expression.set(conditional_and_expression);
        PObject conditional_or_expression = new POr(conditional_and_expression, new PSequence(ptr_conditional_or_expression, new JavaToken("&&"), conditional_and_expression));
        ptr_conditional_or_expression.set(conditional_or_expression);
        PObject conditional_expression = new POr(conditional_or_expression, new PSequence(conditional_or_expression, new PChar('?'), ptr_expression, c_colon, ptr_conditional_expression));
        ptr_conditional_expression.set(conditional_expression);
        PObject assignment_operator = new JavaTokens(new String[] { "=", "*=", "/=", "%=", "+=", "-=", "<<=", ">>=", ">>>=", "&=", "^=", "|=" });
        PObject left_hand_side = new POr(expression_name, field_access, array_access);
        PObject assignment = new PSequence(left_hand_side, assignment_operator, ptr_assignment_expression);
        PObject assignment_expression = new POr(conditional_expression, assignment);
        ptr_assignment_expression.set(assignment_expression);
        PObject expression = assignment_expression;
        ptr_expression.set(expression);
        PObject constant_expression = expression;
        PObject finally_clause = new PSequence(new JavaToken("finally"), ptr_block);
        PObject catch_clause = new PSequence(new JavaToken("catch"), c_parenL, formal_parameter, c_parenR, ptr_block);
        PObject catches = new PMultiple(catch_clause);
        PObject try_statement = new PSequence(new JavaToken("try"), ptr_block, new POr(catches, new PSequence(new POptional(catches), finally_clause)));
        PObject synchronized_statement = new PSequence(new JavaToken("synchronized"), c_parenL, expression, c_parenR, ptr_block);
        PObject throws_statement = new PSequence(new JavaToken("throw"), expression, c_semicolon);
        PObject return_statement = new PSequence(new JavaToken("return"), new POptional(expression), c_semicolon);
        PObject continue_statement = new PSequence(new JavaToken("continue"), new POptional(identifier), c_semicolon);
        PObject break_statement = new PSequence(new JavaToken("break"), new POptional(identifier), c_semicolon);
        PObject statement_expression = new POr(new PObject[] { assignment, preincrement_expression, postincrement_expression, predecrement_expression, postdecrement_expression, method_invocation, class_instance_creation_expression });
        PObject statement_expression_list = new PMultiple(statement_expression);
        PObject for_update = statement_expression_list;
        PObject local_variable_declaration = new PSequence(type, variable_declarators);
        PObject for_init = new POr(statement_expression_list, local_variable_declaration);
        PObject for_statement_no_short_if = new PSequence(new PObject[] { new JavaToken("for"), c_parenL, new POptional(for_init), c_semicolon, new POptional(expression), c_semicolon, new POptional(for_update), c_parenR, ptr_statement_no_short_if });
        PObject for_statement = new PSequence(new PObject[] { new JavaToken("for"), c_parenL, new POptional(for_init), c_semicolon, new POptional(expression), c_semicolon, new POptional(for_update), c_parenR, ptr_statement });
        PObject do_statement = new PSequence(new PObject[] { new JavaToken("do"), ptr_statement, new JavaToken("while"), c_parenL, expression, c_parenR, c_semicolon });
        PObject while_statement_no_short_if = new PSequence(new JavaToken("while"), c_parenL, expression, c_parenR, ptr_statement_no_short_if);
        PObject while_statement = new PSequence(new JavaToken("while"), c_parenL, expression, c_parenR, ptr_statement);
        PObject switch_label = new POr(new PSequence(new JavaToken("case"), constant_expression, c_colon), new PSequence(new JavaToken("default"), c_colon));
        PObject switch_labels = new PMultiple(switch_label);
        PObject switch_block_statement_group = new POr(switch_labels, ptr_block_statements);
        PObject switch_block_statement_groups = new PMultiple(switch_block_statement_group);
        PObject switch_block = new PSequence(c_accolL, new POptional(switch_block_statement_groups), new POptional(switch_labels), c_accolR);
        PObject switch_statement = new PSequence(new JavaToken("switch"), c_parenL, expression, c_parenR, switch_block);
        PObject if_then_else_statement_no_short_if = new PSequence(new PObject[] { new JavaToken("if"), c_parenL, expression, c_parenR, ptr_statement_no_short_if, new JavaToken("else"), ptr_statement_no_short_if });
        PObject if_then_else_statement = new PSequence(new PObject[] { new JavaToken("if"), c_parenL, expression, c_parenR, ptr_statement_no_short_if, new JavaToken("else"), ptr_statement });
        PObject if_then_statement = new PSequence(new JavaToken("if"), c_parenL, expression, c_parenR, ptr_statement);
        PObject expression_statement = new PSequence(statement_expression, c_semicolon);
        PObject labeled_statement_no_short_if = new PSequence(identifier, c_colon, ptr_statement_no_short_if);
        PObject labeled_statement = new PSequence(identifier, c_colon, ptr_statement);
        PObject empty_statement = c_semicolon;
        PObject statement_without_trailing_substatement = new POr(new PObject[] { ptr_block, empty_statement, expression_statement, switch_statement, do_statement, break_statement, continue_statement, return_statement, synchronized_statement, throws_statement, try_statement });
        PObject statement_no_short_if = new POr(new PObject[] { statement_without_trailing_substatement, labeled_statement_no_short_if, if_then_else_statement_no_short_if, while_statement_no_short_if, for_statement_no_short_if });
        ptr_statement_no_short_if.set(statement_no_short_if);
        PObject statement = new POr(new PObject[] { statement_without_trailing_substatement, labeled_statement, if_then_statement, if_then_else_statement, while_statement, for_statement });
        ptr_statement.set(statement);
        PObject local_variable_declaration_statement = new PSequence(local_variable_declaration, c_semicolon);
        PObject block_statement = new POr(local_variable_declaration_statement, statement);
        PObject block_statements = new PMultiple(block_statement);
        ptr_block_statements.set(block_statements);
        PObject block = new PSequence(c_accolL, new POptional(block_statements), c_accolR);
        ptr_block.set(block);
        PObject result_type = new POr(type, new JavaToken("void"));
        PObject method_declarator = new PSequence(identifier, c_parenL, new POptional(formal_parameter_list), c_parenR);
        PObject abstract_method_modifier = new JavaTokens("public", "abstract");
        PObject abstract_method_modifiers = new PMultiple(abstract_method_modifier);
        PObject abstract_method_declaration = new PSequence(new POptional(abstract_method_modifiers), result_type, method_declarator, new POptional(throws_declaration), c_semicolon);
        PObject constant_modifiers = new JavaTokens("public", "static", "final");
        PObject constant_declaration = new PSequence(constant_modifiers, type, variable_declarator);
        PObject interface_member_declaration = new POr(constant_declaration, abstract_method_declaration);
        PObject interface_member_declarations = new PMultiple(interface_member_declaration);
        PObject interface_body = new PSequence(c_accolL, new POptional(interface_member_declarations), c_accolR);
        PObject extends_interfaces = new PSequence(new JavaToken("extends"), new PList(interface_type, c_comma, 0, PList.INFINITE));
        PObject interface_modifier = new JavaTokens("public", "abstract");
        PObject interface_modifiers = new PMultiple(interface_modifier);
        PObject interface_declaration = new PSequence(new POptional(interface_modifiers), new JavaToken("interface"), identifier, new POptional(extends_interfaces), interface_body);
        PObject method_body = new POr(block, c_semicolon);
        PObject method_modifier = new JavaTokens(new String[] { "public", "protected", "private", "static", "abstract", "final", "synchronized", "native" });
        PObject method_modifiers = new PMultiple(method_modifier);
        PObject method_header = new PSequence(new POptional(method_modifiers), result_type, method_declarator, new POptional(throws_declaration));
        PObject method_declaration = new PSequence(method_header, method_body);
        PObject field_modifier = new JavaTokens(new String[] { "public", "protected", "private", "static", "final", "transient", "volatile" });
        PObject field_modifiers = new PMultiple(field_modifier);
        PObject field_declaration = new PSequence(new POptional(field_modifiers), type, variable_declarators, c_semicolon);
        PObject explicit_constructor_invocation = new PSequence(new JavaTokens("this", "super"), c_parenL, new POptional(argument_list), c_parenR);
        PObject constructor_body = new PSequence(c_accolL, new POptional(explicit_constructor_invocation), new POptional(block_statements), c_accolR);
        PObject constructor_declarator = new PSequence(simple_type_name, c_parenL, new POptional(formal_parameter_list), c_parenR);
        PObject constructor_modifier = new JavaTokens("public", "protected", "private");
        PObject constructor_modifiers = new PMultiple(constructor_modifier);
        PObject constructor_declaration = new PSequence(new POptional(constructor_modifiers), constructor_declarator, new POptional(throws_declaration), constructor_body);
        PObject static_initializer = new PSequence(new JavaToken("static"), block);
        PObject class_member_declaration = new POr(field_declaration, method_declaration);
        PObject class_body_declaration = new POr(class_member_declaration, static_initializer, constructor_declaration);
        PObject class_body_declarations = new PMultiple(class_body_declaration);
        PObject class_body = new PSequence(c_accolL, new POptional(class_body_declarations), c_accolR);
        PObject interface_type_list = new PList(interface_type, c_comma);
        PObject interfaces = new PSequence(new JavaToken("implements"), interface_type_list);
        PObject super_class = new PSequence(new JavaToken("extends"), class_type);
        PObject class_modifier = new JavaTokens("public", "abstract", "final");
        PObject class_modifiers = new PMultiple(class_modifier);
        PObject class_declaration = new PSequence(new PObject[] { new POptional(class_modifiers), new JavaToken("class"), identifier, new POptional(super_class), new POptional(interfaces), class_body });
        ptr_type_declarations.set(new POr(class_declaration, interface_declaration, c_semicolon));
        PObject type_import_on_demand_declaration = new PSequence(new JavaToken("import"), package_name, c_dot, c_star, c_semicolon);
        PObject single_type_import_declaration = new PSequence(new JavaToken("import"), type_name, c_semicolon);
        PObject import_declaration = new POr(single_type_import_declaration, type_import_on_demand_declaration);
        ptr_import_declarations.set(new PMultiple(import_declaration));
        ptr_package_declaration.set(new PSequence(new JavaToken("package"), package_name, c_semicolon));
        PObject compilation_unit = new PSequence(new POptional(ptr_package_declaration), new POptional(ptr_import_declarations), new POptional(ptr_type_declarations));
        parser = compilation_unit;
    }

    /**
	 * @see pobs.PObject
	 */
    public PMatch parse(PScanner input, long begin, PContext context) {
        return parser.process(input, begin, context);
    }

    public PMatch parse(PScanner input) {
        return this.parse(input, 0, new PContext(new PDirective(skipper)));
    }
}
