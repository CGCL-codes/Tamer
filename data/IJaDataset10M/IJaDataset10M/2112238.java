package com.cube42.util.instruction;

import java.io.Serializable;
import com.cube42.util.exception.Cube42Exception;
import com.cube42.util.exception.Cube42NullParameterException;

/**
 * Represents a instruction definition.  The instruction definition is responsible
 * for defining all possible instructions that can be created from this definition.
 * A analogy of this is how the DTD is used in XML to describe how the
 * XML is formatted.  In much the same way the InstructionDef defines what
 * arguments and parameters are required for this instruction to be possible.
 *
 * @author Matt Paulin
 * @version $Id: InstructionDef.java,v 1.6 2003/03/12 00:27:54 zer0wing Exp $
 */
public class InstructionDef implements Serializable {

    /**
     * The name associated with this InstructionDef
     */
    private String name;

    /**
     * The description associated with this InstructionDef
     */
    private String description;

    /**
     * ParameterDefs that define all the arguments needed to create this name
     * of instruction
     */
    private ParameterDefCollection arguments;

    /**
     * ParameterDefs that define all the options allowed with this name
     * of instruction
     */
    private ParameterDefCollection options;

    /**
     * Creates an instruction definition
     *
     * @param   name    The name of the instruction defined in this InstructionDef
     * @param   description     A description of what this instruction does
     * @param   arguments       A ParameterDefCollection of all the arguments required by
     *                          this InstructionDef, null if there arn't any arguments
     * @param   options         A ParameterDefCollection of all the options allowed in this
     *                          instruction defintion, null if there arn't any options
     */
    public InstructionDef(String name, String description, ParameterDefCollection arguments, ParameterDefCollection options) {
        super();
        Cube42NullParameterException.checkNullInConstructor(name, "name", this);
        Cube42NullParameterException.checkNullInConstructor(description, "description", this);
        this.name = name;
        this.description = description;
        if (arguments == null) {
            arguments = new ParameterDefCollection();
        }
        this.arguments = arguments;
        if (options == null) {
            options = new ParameterDefCollection();
        }
        this.options = options;
    }

    /**
     * Gets the name from the InstructionDef
     *
     * @return  The name of the InstructionDef
     */
    public String getName() {
        return this.name;
    }

    /**
     * Returns the description of what this InstructionDefinition is
     * used for
     *
     * @return  The description of what this instruction definition is
     *          used for
     */
    public String getDescription() {
        return this.description;
    }

    /**
     * Returns all the arguments associated with this instruction def
     *
     * @return  All the arguments associated with this instruction def
     */
    public ParameterDefCollection getArguments() {
        return this.arguments;
    }

    /**
     * Returns all the options associated with this instruction def
     *
     * @return  All the options associated with this instruction def
     */
    public ParameterDefCollection getOptions() {
        return this.options;
    }

    /**
     * Forms an instruction from the supplied arguments and options
     *
     * @param   argumentParams  The arguments associated with the instruction,
     *                          null if there arn't any
     * @param   optionParams    The options associated with the instruction, null
     *                          if there arn't any
     * @returns An instruction with the supplied arguments and options
     * @throws  Cube42Exception if the arguments and options do not fit this
     *          definition
     */
    public Instruction formInstruction(ParameterCollection argumentParams, ParameterCollection optionParams) throws Cube42Exception {
        if (argumentParams == null) {
            argumentParams = new ParameterCollection();
        }
        if (optionParams == null) {
            optionParams = new ParameterCollection();
        }
        Instruction instruct = new Instruction(this.name, argumentParams, optionParams);
        this.validate(instruct);
        return instruct;
    }

    /**
     * Checks to make sure that a instruction fits this instruction definition
     *
     * @throws Cube42Exception if the instruction is not valid according to this
     *                  InstructionDef
     */
    public void validate(Instruction instruction) throws Cube42Exception {
        Cube42NullParameterException.checkNull(instruction, "instruction", "validate", this);
        if (this.arguments.size() != instruction.getArguments().size()) {
            throw new Cube42Exception(InstructionSystemCodes.WRONG_NUMBER_ARGUMENTS, new Object[] { new Integer(this.arguments.size()), new Integer(instruction.getArguments().size()) });
        }
        if (this.options.size() < instruction.getOptions().size()) {
            throw new Cube42Exception(InstructionSystemCodes.TO_MANY_OPTIONS_PROVIDED, new Object[] { new Integer(this.options.size()), new Integer(instruction.getOptions().size()) });
        }
        Parameter tempParam;
        ParameterDef paramDef;
        for (int i = 0; i < instruction.getArguments().size(); i++) {
            tempParam = instruction.getArguments().getParameterAt(i);
            paramDef = this.arguments.getParameterDef(tempParam.getName());
            if (paramDef == null) {
                throw new Cube42Exception(InstructionSystemCodes.UNKNOWN_ARGUMENT, new Object[] { tempParam.getName() });
            }
            paramDef.validate(tempParam);
        }
        for (int i = 0; i < instruction.getOptions().size(); i++) {
            tempParam = instruction.getOptions().getParameterAt(i);
            paramDef = this.options.getParameterDef(tempParam.getName());
            if (paramDef == null) {
                throw new Cube42Exception(InstructionSystemCodes.UNKNOWN_OPTION, new Object[] { tempParam.getName() });
            }
            paramDef.validate(tempParam);
        }
    }
}
