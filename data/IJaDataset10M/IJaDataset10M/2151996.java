package desmoj.core.dist;

/**
 * Interface to create user-defined operators. Used by ContDistAggregate to
 * combine distribution functions. Note that implementations of common operators
 * are available as static final fields.
 * 
 * @version DESMO-J, Ver. 2.3.4beta copyright (c) 2012
 * @author Peter Wueppen
 * 
 *         Licensed under the Apache License, Version 2.0 (the "License"); you
 *         may not use this file except in compliance with the License. You may
 *         obtain a copy of the License at
 *         http://www.apache.org/licenses/LICENSE-2.0
 * 
 *         Unless required by applicable law or agreed to in writing, software
 *         distributed under the License is distributed on an "AS IS" BASIS,
 *         WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 *         implied. See the License for the specific language governing
 *         permissions and limitations under the License.
 * 
 */
public interface Operator {

    /**
	 * This should return the desired of the result or the operation, depending on the operands.
	 * @param operand1
	 * @param operand2
	 * @return the result
	 */
    public double result(double operand1, double operand2);

    /**
	 * Should return a description of what the operator does to be shown by the reporter. Example: "sum", "product" etc.
	 * @returna a description
	 */
    public String getDescription();

    /**
     * Plus operator, for arguments <code>a<code> and <code>b</code> returning <code>a+b</code>.  
     */
    public static final Operator PLUS = new Operator() {

        public double result(double a, double b) {
            return a + b;
        }

        public String getDescription() {
            return "plus";
        }
    };

    /**
     * Minus operator, for arguments <code>a<code> and <code>b</code> returning <code>a-b</code>.  
     */
    public static final Operator MINUS = new Operator() {

        public double result(double a, double b) {
            return a - b;
        }

        public String getDescription() {
            return "minus";
        }
    };

    /**
     * Multiply operator, for arguments <code>a<code> and <code>b</code> returning <code>a-b</code>.  
     */
    public static final Operator MULTIPLY = new Operator() {

        public double result(double a, double b) {
            return a * b;
        }

        public String getDescription() {
            return "muliply";
        }
    };

    /**
     * Divide operator, for arguments <code>a<code> and <code>b</code> returning <code>a/b</code>.
     */
    public static final Operator DIVIDE = new Operator() {

        public double result(double a, double b) {
            return a / b;
        }

        public String getDescription() {
            return "divide";
        }
    };

    /**
     * Power operator, for arguments <code>a<code> and <code>b</code> returning <code>a^b</code>.  
     */
    public static final Operator POW = new Operator() {

        public double result(double a, double b) {
            return Math.pow(a, b);
        }

        public String getDescription() {
            return "pow";
        }
    };

    /**
     * Absolute difference operator, for arguments <code>a<code> and <code>b</code> returning <code>|a-b|</code>.  
     */
    public static final Operator ABS_DIFF = new Operator() {

        public double result(double a, double b) {
            return Math.abs(a - b);
        }

        public String getDescription() {
            return "diff";
        }
    };
}
