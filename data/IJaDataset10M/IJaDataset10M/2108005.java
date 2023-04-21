package tcl.lang;

class TestEmptyResultCmd implements Command {

    public void cmdProc(Interp interp, TclObject objv[]) throws TclException {
        if (objv.length == 1) {
            interp.resetResult();
            interp.setResult(interp.getResult().isShared());
        } else if (objv.length == 2) {
            int refCount;
            interp.resetResult();
            TclObject null_result = interp.getResult();
            refCount = null_result.getRefCount();
            interp.setResult(null_result);
            if (refCount != interp.getResult().getRefCount()) interp.setResult("setting null result changed ref count");
            interp.setResult("");
            interp.setResult(null_result);
            if (refCount != interp.getResult().getRefCount()) interp.setResult("setting null result after non-null changed ref count");
            interp.setResult("ok");
        }
    }
}
