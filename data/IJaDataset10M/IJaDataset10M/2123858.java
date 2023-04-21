package com.clanwts.nbdf.kernel.nixon;

import java.util.Set;
import com.clanwts.nbdf.kernel.Service;
import com.clanwts.nbdf.kernel.ServiceControl;
import edu.cmu.ece.agora.core.LifecycleState;
import edu.cmu.ece.agora.futures.Future;
import edu.cmu.ece.agora.util.Collections;

class NixonServiceDescriptor {

    public final Class<? extends Service> ifc;

    public final Class<? extends ServiceControl> impl;

    public final ServiceControl inst;

    public long refs;

    public final Set<Service> deps;

    public final NixonServiceContext ctx;

    public LifecycleState state;

    public Future<Boolean> startFuture;

    public Future<Boolean> stopFuture;

    public NixonServiceDescriptor(NixonKernel kernel, Class<? extends Service> ifc, Class<? extends ServiceControl> impl, ServiceControl inst) {
        this.ifc = ifc;
        this.impl = impl;
        this.inst = inst;
        this.refs = 0;
        this.deps = Collections.newHashSet();
        this.ctx = new NixonServiceContext(kernel, kernel.getRootLogger().createSubLogger(ifc.getSimpleName()), ifc);
        this.state = LifecycleState.STOPPED;
        this.startFuture = null;
        this.stopFuture = null;
    }
}
