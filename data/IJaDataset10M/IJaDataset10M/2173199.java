package net.sf.latexdraw.parsers.ps;

import java.util.Deque;

/**
 * Defines the sin command.<br>
 *<br>
 * This file is part of LaTeXDraw<br>
 * Copyright (c) 2005-2012 Arnaud BLOUIN<br>
 *<br>
 *  LaTeXDraw is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation; either version 2 of the License, or
 *  (at your option) any later version.<br>
 *<br>
 *  LaTeXDraw is distributed without any warranty; without even the
 *  implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR
 *  PURPOSE. See the GNU General Public License for more details.<br>
 *<br>
 * 03/11/08<br>
 * @author Arnaud BLOUIN
 * @version 3.0
 */
public class PSSinCommand extends PSArithemticCommand {

    @Override
    public void execute(final Deque<String> stack, final double x) throws InvalidFormatPSFunctionException {
        if (stack == null || stack.isEmpty()) throw new InvalidFormatPSFunctionException();
        stack.push(String.valueOf(Math.sin(Math.toRadians(Double.parseDouble(stack.pop())))));
    }
}
