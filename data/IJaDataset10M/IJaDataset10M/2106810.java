/*
 * $Id: BudgetType.java,v 1.8 2006/03/12 13:20:59 jmanning Exp $
 *
 * Copyright (C) 2001 Jackie Manning j.m@programmer.net
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 *
 */
package jcash;

import java.util.*;
import org.apache.commons.lang.enum.*;

public class BudgetType extends ValuedEnum implements Comparable {

    public static final int ACTUAL_CODE     = 0;
    public static final int PAYABLE_CODE    = 1;
    public static final int BUDGET_CODE     = 2;
    public static final int RECURRING_CODE  = 3;
    public static final int SCHEDULED_CODE  = 4;

    public static final BudgetType ACTUAL
            = new BudgetType( ACTUAL_CODE, "Actual" );
    public static final BudgetType PAYABLE
            = new BudgetType( PAYABLE_CODE, "Payable" );
    public static final BudgetType BUDGET
            = new BudgetType( BUDGET_CODE, "Budget" );
    public static final BudgetType RECURRING
            = new BudgetType( RECURRING_CODE, "Recurr" );
    public static final BudgetType SCHEDULED
            = new BudgetType( SCHEDULED_CODE, "Sched" );

    private BudgetType( int aCode, String aName ) {
        super( aName, aCode );
    }

    public String toString() {
        return getName();
    }

    public static BudgetType getType( int aType ) {
        return getEnum( aType );
    }

    public static BudgetType getEnum( String acctType ) {
        return ( BudgetType ) getEnum( BudgetType.class, acctType );
    }

    public static BudgetType getEnum( int acctType ) {
        return ( BudgetType ) getEnum( BudgetType.class, acctType );
    }

    public static Map getEnumMap() {
        return getEnumMap( BudgetType.class );
    }

    public static List getEnumList() {
        return getEnumList( BudgetType.class );
    }

    public static Iterator iterator() {
        return iterator( BudgetType.class );
    }

    public int compareTo( Object o ) {
        return this.toString().compareTo( o );
    }

}
