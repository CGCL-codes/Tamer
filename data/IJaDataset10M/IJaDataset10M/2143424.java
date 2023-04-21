package gp;

class Subtraction extends Function {

    Subtraction() {
        arg = new Program[2];
    }

    String getName() {
        return "sub";
    }

    double eval(double x) {
        return arg[0].eval(x) - arg[1].eval(x);
    }
}
