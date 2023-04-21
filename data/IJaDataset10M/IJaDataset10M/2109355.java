package br.mips.types;

public abstract class Types {

    public static final String INSTRUCTIONS[] = { "add", "addi", "beq", "bne", "exit", "j", "jal", "jr", "lb", "lui", "lw", "sb", "slt", "slti", "sub", "sw", "syscall" };

    public static final String DIRECTIVES[] = { ".asciiz", ".word" };

    public static final String REGISTERS[] = { "$zero", "$at", "$v0", "$v1", "$a0", "$a1", "$a2", "$a3", "$t0", "$t1", "$t2", "$t3", "$t4", "$t5", "$t6", "$t7", "$s0", "$s1", "$s2", "$s3", "$s4", "$s5", "$s6", "$s7", "$t8", "$t9", "$k0", "$k1", "$gp", "$sp", "$fp", "$ra" };

    public static final String LABEL_CHARS = "01234567890ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz_";
}
