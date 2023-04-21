package br.net.woodstock.rockframework.runtime;

class OutputImpl implements Output {

    private static final long serialVersionUID = -5028254907646518037L;

    private String out;

    private String err;

    public OutputImpl(final String out, final String err) {
        super();
        this.out = out;
        this.err = err;
    }

    @Override
    public String getOut() {
        return this.out;
    }

    @Override
    public String getErr() {
        return this.err;
    }
}
