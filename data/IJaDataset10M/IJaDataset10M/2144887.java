package net.sf.jfling.basetypes;

import java.util.ArrayList;
import java.util.List;
import net.sf.jfling.FixedLengthWrapper;
import org.apache.commons.lang.StringUtils;

/**
 * Copyright 2009 Philip Fitzsimmons (philipfitzsimmons@hotmail.com)
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may
 * obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions
 * and limitations under the License.
 * 
 * 
 * @author Philip Fitzsimmons (philipfitzsimmons@hotmail.com)
 * 
 */
public class FixedLengthListWrapper<T extends FixedLengthWrapper> extends ArrayList<T> implements FixedLengthWrapper<FixedLengthListWrapper<T>> {

    private static final long serialVersionUID = -6495482529040453973L;

    protected int numElementsPrefixLength;

    Class<? extends FixedLengthWrapper> type;

    int numberOfMembers;

    String numberOfMembersAsString;

    private boolean fixedNumberOfMembers = false;

    /**
	 * The constructor you'll get if your CSV file has an entry like this:<br/>
	 * <code>Contract Ranges	list	2		TS09AvailableMarginAndConditionsContractRangeVO</code><br/>
	 * which would tell this implementation to employ the first two characters of the input string to figure out how many children it has.
	 * 
	 * @param len
	 * @param class1
	 */
    public <T extends FixedLengthWrapper> FixedLengthListWrapper(final int numElementsPrefixLength, final Class<T> class1) {
        if ((numElementsPrefixLength < 0) || (numElementsPrefixLength > 255)) {
            this.numElementsPrefixLength = 2;
        } else if (numElementsPrefixLength == 0) {
            this.fixedNumberOfMembers = false;
            this.numElementsPrefixLength = numElementsPrefixLength;
        } else {
            this.numElementsPrefixLength = numElementsPrefixLength;
        }
        this.type = class1;
    }

    /***
	 * The constructor you'll get if you have a entry in our CSV file like this:<br/>
	 * <code>hours	list	0		BranchInformationOpeningHoursVO	3	</code><br/>
	 * This would tell the instance that it can expect to extract three members from the fixed length string.
	 * 
	 * @param len
	 * @param class1
	 * @param numberOfMembers
	 */
    public <T extends FixedLengthWrapper> FixedLengthListWrapper(final int len, final Class<T> class1, final int numberOfMembers) {
        this.numberOfMembers = numberOfMembers;
        this.fixedNumberOfMembers = true;
        this.type = class1;
        this.numElementsPrefixLength = 0;
    }

    /***
	 * The constructor you'll get if you have a entry in our CSV file like this:<br/>
	 * <code>hours	list	0		BranchInformationOpeningHoursVO	3	</code><br/>
	 * This would tell the instance that it can expect to extract three members from the fixed length string.
	 * 
	 * @param len
	 * @param class1
	 * @param numberOfMembers
	 */
    public <T extends FixedLengthWrapper> FixedLengthListWrapper(final int len, final String format, final Class<T> class1) {
        this.numberOfMembers = Integer.parseInt(format);
        this.fixedNumberOfMembers = true;
        this.type = class1;
        this.numElementsPrefixLength = 0;
    }

    public int consumeFixedLengthString(final String string) {
        this.getValue().clear();
        final int numberOfMembersCurr = getNumberOfMembers(string);
        FixedLengthWrapper<?> instance;
        int lengthConsumed = this.numElementsPrefixLength;
        if (numberOfMembersCurr >= 0) {
            for (int i = 0; i < numberOfMembersCurr; i++) {
                final int previousIndex = lengthConsumed;
                instance = getMemberInstance();
                this.add((T) instance);
                lengthConsumed += instance.consumeFixedLengthString(string.substring(lengthConsumed));
            }
        } else {
            try {
                while (lengthConsumed != string.length()) {
                    final int previousIndex = lengthConsumed;
                    instance = getMemberInstance();
                    lengthConsumed += instance.consumeFixedLengthString(string.substring(lengthConsumed));
                    this.add((T) instance);
                }
            } catch (final IllegalArgumentException e) {
                return lengthConsumed;
            }
        }
        return getLength();
    }

    public String getFixedLengthString() {
        final StringBuffer stringBuffer = new StringBuffer();
        if (this.numElementsPrefixLength > 0) {
            stringBuffer.append(StringUtils.leftPad(size() + "", this.numElementsPrefixLength, '0'));
        }
        for (final FixedLengthWrapper<?> wrap : (List<FixedLengthWrapper>) this) {
            stringBuffer.append(wrap.getFixedLengthString());
        }
        return stringBuffer.toString();
    }

    /**
	 * {@inheritDoc}
	 */
    public int getLength() {
        int length = 0;
        length += this.numElementsPrefixLength;
        for (final FixedLengthWrapper wrapper : this) {
            length += wrapper.getLength();
        }
        return length;
    }

    public String getTreeView() {
        String result = "|-'" + this.getClass() + "' \n";
        for (final FixedLengthWrapper<?> wrapper : this) {
            result += addTabs(wrapper.getTreeView());
        }
        result += "\n";
        return result;
    }

    public FixedLengthListWrapper<T> getValue() {
        return this;
    }

    /**
	 * {@inheritDoc}
	 */
    public void setFixedLengthString(final String string) {
        final int index = consumeFixedLengthString(string);
        if (index != string.length()) throw new IllegalArgumentException("String set for FixedLengthListWrapper has incorrect length. Set string input: '" + string + "' has length '" + string.length() + "' while expecting a length of: '" + getLength() + "'");
    }

    /**
	 * FixedLengthListWrapper is an implementation of List, so setting its value first clears any members already in the list and then
	 * supers to addAll.
	 */
    public void setValue(final FixedLengthListWrapper<T> value) {
        this.clear();
        addAll(value);
    }

    protected FixedLengthWrapper<?> getMemberInstance() {
        FixedLengthWrapper<?> result = null;
        try {
            result = this.type.newInstance();
        } catch (final IllegalAccessException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } catch (final InstantiationException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return result;
    }

    /**
	 * adds a tab before each line in the given string
	 * 
	 * @param string
	 * @return
	 */
    private String addTabs(final String string) {
        final String[] lines = string.split("\\n");
        String result = "";
        for (final String s : lines) {
            result += "  " + s + "\n";
        }
        return result;
    }

    private int getNumberOfMembers(final String string) {
        if (this.numElementsPrefixLength > 0) {
            this.numberOfMembers = Integer.parseInt(new String(string.substring(0, this.numElementsPrefixLength)));
        } else if (this.fixedNumberOfMembers) return this.numberOfMembers; else return -1;
        return this.numberOfMembers;
    }
}
